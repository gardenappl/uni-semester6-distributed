package main

import (
	"fmt"
	"math/rand"
)

const totalItems = 15
const bufferSize = 3

var stolenItemsCount = 0

type ArmyItem struct {
	id int
}

func (item ArmyItem) String() string {
	return fmt.Sprintf("Item #%d", item.id)
}

func makeArmyItem(i int) ArmyItem {
	return ArmyItem{id: i*100 + rand.Intn(100)}
}

func steal(stolenItems chan ArmyItem) {
	for i := 0; i < totalItems; i++ {
		var stolenItem = makeArmyItem(i)
		stolenItems <- stolenItem
		fmt.Printf("Stole %s\n", stolenItem)
	}
	close(stolenItems)
}

func loadOnTruck(stolenItems chan ArmyItem, truckItems chan ArmyItem) {
	for item := range stolenItems {
		truckItems <- item
		fmt.Printf("Loaded %s on truck\n", item)
	}
	close(truckItems)
}

func countFromTruck(truckItems chan ArmyItem, done chan bool) {
	for range truckItems {
		stolenItemsCount++
		fmt.Printf("Stolen items count: %d\n", stolenItemsCount)
	}
	done <- true
}

func main() {
	var stolenItems = make(chan ArmyItem, bufferSize)
	var truckItems = make(chan ArmyItem, bufferSize)
	var done = make(chan bool, 1)

	go steal(stolenItems)
	go loadOnTruck(stolenItems, truckItems)
	go countFromTruck(truckItems, done)
	<-done
}
