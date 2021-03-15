package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"sync"
	"time"
)

const fileName = "db.txt"

func searchBySurname(surname string, lock *sync.RWMutex) {
	fmt.Printf("Searching for %s: acquiring read lock...\n", surname)
	lock.RLock()
	defer lock.RUnlock()
	fmt.Printf("Searching for %s: acquired read lock.\n", surname)

	file, err := os.Open(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		line := scanner.Text()
		if err := scanner.Err(); err != nil {
			log.Fatal(err)
		}
		time.Sleep(100 * time.Millisecond)

		var f, i, o, tel string
		n, err := fmt.Sscanf(line, "%s %s %s - %s", &f, &i, &o, &tel)
		if err != nil {
			log.Fatal(err)
		}
		if n < 4 {
			log.Fatal("Could not parse line: ", line)
		}
		if f == surname {
			fmt.Printf("Found %s for %s %s %s\n", tel, f, i, o)
		}
	}
	fmt.Printf("Done searching for %s.\n", surname)
}

func searchByNumber(phoneNumber string, lock *sync.RWMutex) {
	fmt.Printf("Searching for %s: acquiring read lock...\n", phoneNumber)
	lock.RLock()
	defer lock.RUnlock()
	fmt.Printf("Searching for %s: acquired read lock.\n", phoneNumber)

	file, err := os.Open(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		line := scanner.Text()
		if err := scanner.Err(); err != nil {
			log.Fatal(err)
		}
		time.Sleep(100 * time.Millisecond)

		var f, i, o, tel string
		n, err := fmt.Sscanf(line, "%s %s %s - %s", &f, &i, &o, &tel)
		if err != nil {
			log.Fatal(err)
		}
		if n < 4 {
			log.Fatal("Could not parse line: ", line)
		}
		if phoneNumber == tel {
			fmt.Printf("Found owner of %s: %s %s %s\n", tel, f, i, o)
		}
	}
	fmt.Printf("Done searching for %s.\n", phoneNumber)
}

func deleteSurname(surname string, lock *sync.RWMutex) {
	fmt.Printf("Deleting %s: acquiring write lock...\n", surname)
	lock.Lock()
	defer lock.Unlock()
	fmt.Printf("Deleting %s: acquired write lock.\n", surname)

	oldFile, err := os.Open(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer oldFile.Close()

	newFile, err := os.Create(fileName + ".new")
	if err != nil {
		log.Fatal(err)
	}
	defer newFile.Close()

	scanner := bufio.NewScanner(oldFile)

	for scanner.Scan() {
		line := scanner.Text()
		if err := scanner.Err(); err != nil {
			log.Fatal(err)
		}
		time.Sleep(100 * time.Millisecond)

		var f, i, o, tel string
		n, err := fmt.Sscanf(line, "%s %s %s - %s", &f, &i, &o, &tel)
		if err != nil {
			log.Fatal(err)
		}
		if n < 4 {
			log.Fatal("Could not parse line: ", line)
		}
		if surname != f {
			fmt.Fprintln(newFile, line)
		} else {
			fmt.Printf("Deleting %s...\n", line)
		}
	}

	oldFile.Close()
	newFile.Close()
	
	err = os.Rename(fileName + ".new", fileName)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Printf("Done deleting %s.\n", surname)
}

func appendEntry(f, i, o, tel string, lock *sync.RWMutex) {
	fmt.Printf("Adding %s %s %s, acquiring write lock...\n", f, i, o)
	lock.Lock()
	defer lock.Unlock()
	fmt.Printf("Adding %s %s %s: acquired write lock...\n", f, i, o)

	file, err := os.OpenFile(fileName, os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0600)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	time.Sleep(100 * time.Millisecond)
	_, err = fmt.Fprintf(file, "%s %s %s - %s\n", f, i, o, tel)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Printf("Done adding %s %s %s.\n", f, i, o)
}

func surnameSearcher(surname string, lock *sync.RWMutex) {
	for {
		time.Sleep(1000)
		searchBySurname(surname, lock)
	}
}

func phoneSearcher(phoneNumber string, lock *sync.RWMutex) {
	for {
		time.Sleep(1000)
		searchByNumber(phoneNumber, lock)
	}
}

func entryRemover(surname string, lock *sync.RWMutex) {
	for {
		time.Sleep(2000)
		deleteSurname(surname, lock)
	}
}

func entryAdder(f, i, o, tel string, lock *sync.RWMutex) {
	for {
		time.Sleep(1000)
		
		appendEntry(f, i, o, tel, lock)
	}
}

func main() {
	lock := sync.RWMutex{}
	
	go entryAdder("Hryshchenko", "Yurii", "Anataoliiovych", "1111111", &lock)
	go entryAdder("Petrenko", "Petro", "Petrovych", "5555555", &lock)
	go entryRemover("Hryshchenko", &lock)
	go entryRemover("Petrenko", &lock)
	go surnameSearcher("Hryshchenko", &lock)
	go phoneSearcher("5555555", &lock)

	fmt.Scanln()
}