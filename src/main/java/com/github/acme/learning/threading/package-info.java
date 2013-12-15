
/**
 *
 * f(n) = f(n-2) + f(n-1)
 *
 * f(i), i << k, f(k)
 * f(10) = f(5) + diff_i_to_k(i, k)
 *
 * 1. Simple, straightforward implementation (one thread).
 * 2. Multi-threaded implementation: when compute element K, start a thread
 * for K-1 and K-2 and notify parent for result.
 * 3. Multi-threaded implementation: data holder for computed values and
 * calculate missing values (positions) when a new number is requested.
 * 4. another implementation that does not block the caller
 * (calling computeFibonacciAtIndex from a thread does not block).
 *   f(15) (starts at 0s, takes 5s),
 *   f(20) (starts at 4s, takes 8s) (5s' + 1s)
 *
 */