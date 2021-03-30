package main

import (
	"fmt"
	"math/rand"
	"runtime"
	"sync"
	"time"
)

type Barrier struct {
	arrivedParties int
	totalParties   int
	wokeUpParties  int
	lock           sync.Mutex
	barrierChan    chan int
	runnable       func()
}

func NewBarrier(parties int, runnable func()) *Barrier {
	b := Barrier{
		arrivedParties: 0,
		totalParties:   parties,
		barrierChan:    make(chan int, parties),
		runnable:       runnable,
		wokeUpParties:  0,
	}
	return &b
}

func (b *Barrier) Await() {
	for b.arrivedParties == b.totalParties {
		//threads are still waking up...
		runtime.Gosched()
	}

	b.lock.Lock()
	b.arrivedParties += 1
	//First thread to exit barrier
	if b.arrivedParties == b.totalParties {
		b.runnable()
		b.wokeUpParties = 0
		for i := 0; i < b.totalParties; i++ {
			b.barrierChan <- 1
		}
	}
	b.lock.Unlock()

	<-b.barrierChan

	b.lock.Lock()
	b.wokeUpParties++
	//Last thread to exit barrier
	if b.wokeUpParties == b.totalParties {
		b.arrivedParties = 0
	}
	b.lock.Unlock()
}

func stringManipulator(index int, s []byte, abCounts []int, barrier *Barrier, wg *sync.WaitGroup) {
manipulatorLoop:
	for true {
		barrier.Await()

		abCounts[index] = 0
		for i := 0; i < len(s); i++ {
			if s[i] == 'A' {
				abCounts[index]++
			} else if s[i] == 'B' {
				abCounts[index]++
			}
		}

		barrier.Await()

		isHighest, highest := true, abCounts[index]
		for i := 0; i < len(abCounts); i++ {
			//Check if any 3 strings match
			matchCount := 0
			for j := 0; j < len(abCounts); j++ {
				if abCounts[i] == abCounts[j] {
					matchCount++
				}
				if matchCount == 3 {
					break manipulatorLoop
				}
			}
			if i != index {
				if abCounts[i] > highest {
					isHighest = false
					highest = abCounts[i]
				}
			}
		}

		//fmt.Printf("%d: %d, is highest? %t\n", index, abCounts[index], isHighest)
		for i := 0; i < len(s); i++ {
			if isHighest {
				if s[i] == 'A' {
					s[i] = 'C'
					break
				} else if s[i] == 'B' {
					s[i] = 'D'
					break
				}
			} else if abCounts[index]-highest >= 2 {
				if s[i] == 'C' {
					s[i] = 'A'
					break
				} else if s[i] == 'D' {
					s[i] = 'B'
					break
				}
			}
		}
	}
	//fmt.Printf("[%d] Done.\n", index)
	wg.Done()
}

func main() {
	rand.Seed(time.Now().UTC().UnixNano())
	wg := new(sync.WaitGroup)
	wg.Add(4)

	abCounts := [4]int{}
	strings := [4][]byte{}

	barrier := NewBarrier(4, func() {
		for i := 0; i < 4; i++ {
			fmt.Printf("%s (AB: %d), ", strings[i], abCounts[i])
		}
		fmt.Println()
	})

	abcd := [4]byte{'A', 'B', 'C', 'D'}
	for i := 0; i < 4; i++ {
		strings[i] = make([]byte, 20)
		for j := 0; j < 20; j++ {
			strings[i][j] = abcd[int(rand.Float32()*4)]
		}

		go stringManipulator(i, strings[i], abCounts[:], barrier, wg)
	}

	wg.Wait()
}
