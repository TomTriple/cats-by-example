import cats.effect.IOApp
import cats.effect.IO
import cats.effect.ExitCode
import cats.implicits._

object MainTutEither extends IOApp {

  /* 

  import scala.util._ 
  sealed abstract class Either[+E, +A]
  case class Left[+E, +A](value: E) extends Either[E, A]
  case class Right[+E, +A](value: A) extends Either[E, A]

  – Either encodes that functions may return a failure value (E) or the correct value (A)
  – Either.right(...) = The "right"/"correct" value -> "smart constructor" for Right(...)
  – Either.left(...)  = The failure value           -> "smart constructor" for Left(...)  
  – Note that there are usually not many reasons to pattern match on an Either
  – Good news: Either forms a monad - you can compose multiple Eithers

  */

  type AppResult[A] = Either[Throwable, A]

  def validateNonEmpty(str:String):AppResult[String] = {
    if(str.length() > 0)
      Either.right(str)
    else 
      Either.left(new Throwable("str is empty"))
  }

  def validateMin(min:Int, str:String):AppResult[String] = 
    Either.cond(str.size >= min, str, new Throwable(s"str has less than $min characters"))

  def validateMax(max:Int, str:String):AppResult[String] = 
    Either.cond(str.size <= max, str, new Throwable(s"str has more than $max characters"))

  def validateBetween(min:Int, max:Int, str:String):AppResult[String] = for {
    a <- validateMin(min, str)
    b <- validateMax(max, a)
  } yield b
  
  def run(args: List[String]): IO[ExitCode] = {
    IO { 

      val result1 = validateNonEmpty("test")
      // println(result)

      val result2 = validateMin(3, "test")
      // println(result2)

      val result3 = validateBetween(1, 10, "")
      // println(result3)

      // println(Either.catchNonFatal("55".toInt))

      // println(Either.fromOption(None, new Throwable("error")))

      val result4 = validateBetween(1, 10, "test")

      val result5 = result4.flatMap { str => Either.cond(str.length() >= 5, str, new Throwable("str is smaller than 5")) }
      //println(result5)

      val result6 = result5
        .leftMap(th => th.getMessage())
        .fold(it => "value has errors: " + it, it => "value was correct: " + it)
      println(result6)


    }.as(ExitCode.Success)
  }
}

