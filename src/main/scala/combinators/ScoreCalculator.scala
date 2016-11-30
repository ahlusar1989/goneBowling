package saran.goneBowling.combinators


object ScoreCalculator {


  def apply(sheet: ScoreSheet): Int = total(sheet.frames, sheet.bonusRound)

  def apply(sheet: String, frameCnt: Int): Int = {
    apply(ScoreCombinatorParser(sheet, frameCnt))
  }

  def total(frames: List[Frame], bonus: List[Int]): Int = frames match {
    case Nil => 0
    case _ :: rest => scoreFirst(frames, bonus) + total(rest, bonus)
  }

  def scoreFirst(frames: List[Frame], bonus: List[Int]): Int = {
    val firstFrame = frames.head
    firstFrame.first + firstFrame.second.getOrElse(0) + bonusRoundScore(numberOfBonus(firstFrame), frames.tail, bonus)
  }

  def bonusRoundScore(rolls: Int, frames: List[Frame], bonus: List[Int]): Int = frames match {
    case Nil => bonus.sum
    case _ => bonusRolls(rolls, frames, bonus).sum
  }

  def numberOfBonus(frame: Frame): Int = frame match {
    case Frame(10, None) => 2
    case Frame(frame, Some(second)) if frame + second == 10 => 1
    case _ => 0
  }

  def bonusRolls(rolls: Int, frames: List[Frame], bonus: List[Int]): List[Int] = (rolls, frames) match {
    case (0, _) => Nil
    case (1, Nil) => bonus.head :: Nil
    case (1, _) => frames.head.first :: Nil
    case (2, Nil) => bonus
    case (2, _) if frames.head.second.isDefined => frames.head.first :: frames.head.second.get :: Nil
    case (2, _) => frames.head.first :: bonusRolls(1, frames.tail, bonus)
    case _ => throw new IllegalArgumentException
  }
}