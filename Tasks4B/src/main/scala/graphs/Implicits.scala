package graphs

object Implicits {
  implicit class IterableSumBy[A](x: Iterable[A]) {
    def sumBy[B : Numeric](f: A => B): B = {
      val num = implicitly[Numeric[B]]
      x.foldLeft(num.zero) {
        case (sum, elem) => num.plus(sum, f(elem))
      }
    }
  }
}
