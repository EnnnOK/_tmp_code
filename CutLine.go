package main

import (
	"errors"
	"fmt"
	"math"
	"math/rand"
)

func main() {
	// TODO 整型，浮点越界问题
	for i := 0; i < 10000; i++ {
		l := rand.Int31n(1000)
		p := rand.Int31n(20) + 1
		Divide2RandPart(l, p)
	}
}

func Divide2RandPart(lenth int32, part int32) (randArray []int32, err error) {

	departLength := lenth

	if part >= departLength {
		return nil, errors.New("part is bigger then length")
	}

	if part <= 0 {
		return nil, errors.New("cannot divide to 0")
	}
	randArray = make([]int32, part)

	var i int32
	for i = 0; i < part-1; i++ {
		rand := rand.Int31n(departLength) - 1
		randArray[i] = rand
		departLength -= rand
	}

	randArray[part-1] = departLength

	var sum int32
	for _, item := range randArray {
		sum += item
	}
	fmt.Println(GetVarianceOfArray(randArray,lenth))

	return randArray, nil
}

func GetMaxMinFromArray(array []int32) (max int32, min int32) {
	//TODO Goroutine
	max = array[0]
	min = array[0]
	for _, item := range array {
		if item > max {
			max = item
		}
		if item < min {
			min = item
		}
	}
	return max, min
}

func GetVarianceOfArray(array []int32, length int32) (variance float64) {
	var average float64
	average = float64(length / int32(len(array)))

	var sum float64
	for _, item := range array {
		itemMinusAverageSqure := math.Pow(float64(item)-average, 2)
		itemMinusAverageSqure = itemMinusAverageSqure / float64(len(array))
		sum += itemMinusAverageSqure
	}
	return sum
}
