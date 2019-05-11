package main

import (
	"errors"
	"fmt"
	"unsafe"
)

func main() {
	a := int64(112223)
	b := int64(231224)
	c := int64(1231)

	AddValue(a, arr)
	AddValue(b, arr)
	AddValue(c, arr)

	fmt.Println(ExistValue(a, arr))
	fmt.Println(ExistValue(b, arr))
	fmt.Println(ExistValue(c, arr))
	fmt.Println(ExistValue(int64(1231892), arr))

	b_v := true
	barr := []bool{true, true, false, false, true, true, false, false}
	i8_v := int8(12)
	i8arr := []int8{int8(1), int8(2), int8(3), int8(4), int8(5), int8(6), int8(7), int8(8)}
	fmt.Println(unsafe.Sizeof(b_v))
	fmt.Println(unsafe.Sizeof(i8_v))
	fmt.Println(unsafe.Sizeof(barr))
	fmt.Println(unsafe.Sizeof(i8arr))

}

// TODO 优化结构，用一个uint8整型表示255个位置
var arr = make([]int, 10000000, 10000000)

func AddValue(value int64, array []int) error {
	if int64(len(array)) < value {
		return errors.New("exceed max")
	}
	array[value] = 1
	return nil
}

func ExistValue(value int64, array []int) (bool, error) {
	if int64(len(array)) < value {
		return false, errors.New("exceed max")
	}
	return array[value] == 1, nil
}
