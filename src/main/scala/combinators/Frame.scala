package saran.goneBowling.combinators

//there will always be a first but we need to take into account that the second is Optional.
// ...here Options can come into play

case class Frame(first: Int, second: Option[Int] = None)


object Frame {
  def apply(first: Int, second: Int): Frame = apply(first, Some(second))
}