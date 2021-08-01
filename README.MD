# Absolutely Use~~full~~less Language

language inspired of my view of functional programming and two day off

THINGS THAT WE DONT TOLERATE:
1) null, so either 0
2) currying, ~~because some problems with parser~~
3) anonymous values
4) using that languages

some examples of programs:

```kotlin
fun min(first, second) = {
    if (first < second) {
        const c = first;
    } else {
        const c = second;
    }
    return c;
}

fun minWithFive(second) = {
    const five = 5;
    const res = min(five, second);
    return res;
}

const one = 1;
const minFromOneAndFive = minWithFive(one);
print(minFromOneAndFive);

--------INPUT---------
1.0
```

```kotlin
fun fib(num) = {
    if (num < 1) {
        const fibRes = 1 - 1;
    } else {
        if (num < 3) {
            const fibRes = 1;
        } else {
            const numMinusOne = num - 1;
            const numMinusTwo = num - 2;
            const fibMinusOne = fib(numMinusOne);
            const fibMinusTwo = fib(numMinusTwo);
            const fibRes = fibMinusOne + fibMinusTwo;
        }
    }
    return fibRes;
}

const ten = 10;
const fibOfTen = fib(ten);
print(fibOfTen);

--------INPUT---------
55.0
```

```kotlin
fun map(n, mapper) = {
    if (n < 1) {
        const zero = 1 - 1;
        const res = mapper(zero);
    } else {
        const mapped = mapper(n);
        print(mapped);
        const nMinusOne = n - 1;
        const res = map(nMinusOne, mapper);
    }
    return res;
}

fun plusOneMapper(n) = {
    const nPlusOne = n + 1;
    return nPlusOne;
}

fun squareMapper(n) = {
    const square = n * n;
    return square;
}

const five = 5;
const mapResult = map(five, squareMapper);
print(mapResult);
--------INPUT---------
25.0
16.0
9.0
4.0
1.0
0.0
```

## Enjoy not using it!
