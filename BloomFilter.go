package main

import (
	"errors"
	"fmt"
	"math/rand"
)

func main() {
	p1 := "apdo"
	p2 := "benji"
	p3 := "dopa"
	p4 := "cuzz"
	p5 := "uzi"
	p6 := "faker"
	p7 := "amateur"
	SetString(p1)
	SetString(p2)
	SetString(p3)
	SetString(p4)
	SetString(p5)
	SetString(p6)
	SetString(p7)

	fmt.Println(CheckString(p1))
	fmt.Println(CheckString(p2))
	fmt.Println(CheckString(p3))
	fmt.Println(CheckString(p4))
	fmt.Println(CheckString(p5))
	fmt.Println(CheckString(p6))
	fmt.Println(CheckString(p7))
	fmt.Println(CheckString("ZZZZZZZZ"))
}

// TODO 优化
var hashSeeds = []int8{3, 5, 7, 9, 11, 13, 17, 19, 23, 27, 29, 31}

var array = make([]uint8, 1000, 1000)

const maxSeedNum = 50

// TODO 考虑采用随机获得Seed方法？
func getRandomHashSeed(seedNum int32) ([]int64, error) {
	if seedNum > maxSeedNum {
		return nil, errors.New("exceed max seed number")
	}
	seeds := make([]int64, seedNum, seedNum)

	for i := 0; int32(i) < seedNum; i++ {
		r := int64(rand.Int31n(maxSeedNum))
		// TODO 检查重复
		seeds[i] = r
	}
	return seeds, nil
}

func hash(hashSeed int8, s string) (hashValue int64) {
	// TODO int越界处理，大致计算string的长度，考虑换成uint？
	for _, c := range s {
		hashValue += int64(hashSeed)*hashValue + int64(c)
	}
	return hashValue
}

func SetString(s string) error {
	// TODO 最大string长度检查，或者考虑使用前缀查询。
	if len(s) > 20 {
		return errors.New("exceed max string length")
	}
	for _, seed := range hashSeeds {
		value := hash(seed, s)
		position := value % (int64(len(array)) * 7)
		bucket := position / int64(len(array))
		bitPos := uint(position % int64(7))
		a := array[bucket] | (1 << bitPos)
		array[bucket] = a
	}
	return nil
}

func CheckString(s string) (exist bool) {
	exist = true
	for _, seed := range hashSeeds {
		value := hash(seed, s)
		position := value % (int64(len(array)) * 7)
		bucket := position / int64(len(array))
		bitPos := uint(position % int64(7))
		if (array[bucket] & (1 << bitPos)) == 0 {
			return !exist
		}
	}
	return exist
}

func init() {
	//在这里初始化seeds的操作
}
