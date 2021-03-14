package main

import (
	"context"
	"fmt"
	"golang.org/x/sync/semaphore"
	"sync/atomic"
	"time"
)

const maxHoney = 10
const beesCount = 3

type winnie struct {
	semaphore *semaphore.Weighted
	honey     int32
	honeyFull chan bool
}

func makeWinnieThePooh() winnie {
	return winnie{
		semaphore: semaphore.NewWeighted(maxHoney),
		honey: 0,
		honeyFull: make(chan bool),
	}
}

func (w *winnie) simulate() {
	for {
		fmt.Println("Winnie: ZZZ...")

		var isFull, ok = <-w.honeyFull
		if !ok || !isFull {
			break
		}

		fmt.Println("Winnie: Dinner time!")
		w.honey = 0

		w.semaphore.Release(maxHoney)
	}

}

func simulateBee(ctx context.Context, id int, w *winnie) {
	for {
		fmt.Printf("Bee %d: Searching for honey...\n", id)

		time.Sleep(200 * time.Millisecond)

		_ = w.semaphore.Acquire(ctx, 1)

		var newHoney = atomic.AddInt32(&w.honey, 1)
		fmt.Printf("Bee %d: Added honey, now %d\n", id, newHoney)

		if newHoney == maxHoney {
			fmt.Printf("Bee %d: Wake up!\n", id)
			w.honeyFull <- true
		}
	}
}

func main() {
	var thePooh = makeWinnieThePooh()
	go thePooh.simulate()

	var ctx = context.Background()
	for i := 0; i < beesCount; i++ {
		go simulateBee(ctx, i, &thePooh)
	}

	//Press any key to exit
	_, _ = fmt.Scanln()
}

