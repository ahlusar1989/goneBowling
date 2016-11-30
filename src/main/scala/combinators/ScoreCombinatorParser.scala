package saran.goneBowling.combinators

import util.parsing.combinator.RegexParsers

//I decided to use parser combinators:

//p1 ~ p2 // sequencing: must match p1 followed by p2
//p1 | p2 // alternation: must match either p1 or p2, with preference given to p1
//p1.?    // optionality: may match p1 or not
//p1.*    // repetition: matches any number of repetitions of p1


class ScoreCombinatorParser(frameCnt: Int) extends RegexParsers  {

  def record: Parser[ScoreSheet] = repN(frameCnt, frame) ~ bonus ^^ {
    case frames~bonus => ScoreSheet(frames, bonus)
  }

  def frame: Parser[Frame] = strike | spare | open

  def strike: Parser[Frame] = "X" ^^ {
    case "X" => Frame(10)
  }

  def spare: Parser[Frame] = c<~"/" ^^ {
    case c1 => Frame(c1, 10-c1)
  }

  def open: Parser[Frame] = c~c ^^ {
    case c1 ~ c2 => Frame(c1, c2)
  }

  def bonus: Parser[List[Int]] = spareAsList | rep(c)
  def spareAsList: Parser[List[Int]] = spare ^^ {
    case frame => List(frame.first, frame.second.get)
  }

  def c: Parser[Int] = digit | dash | x

  def digit: Parser[Int] = """\d""".r ^^ {
    case s if s.length == 1 => s.head - '0'
    case _ => throw new IllegalArgumentException
  }

  def dash: Parser[Int] = "-" ^^^ {
    0
  }

  def x: Parser[Int] = "X" ^^^ {
    10
  }

  def apply(in: String): ScoreSheet = parseAll(record, in) match {
    case Success(result, _) => result
    case NoSuccess(msg, _) => throw new IllegalArgumentException(s"Parsing error: $msg")
  }
}

object ScoreCombinatorParser {
  def apply(in: String, frameCnt: Int) = {
    val parser = new ScoreCombinatorParser(frameCnt)
    parser(in)
  }

}