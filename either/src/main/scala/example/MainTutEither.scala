import cats.effect.IOApp
import cats.effect.IO
import cats.effect.ExitCode
import cats.implicits._
import scala.util.Try

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

  type Parse[A] = Either[Throwable, A]

  def integer1(str:String):Parse[Int] = Try(str.toInt).toEither

  def integer2(str:String):Parse[Int] = Either.catchNonFatal(str.toInt)

  def add(str1:String, str2:String) = for {
    a <- integer1(str1)
    b <- integer1(str2)
  } yield a + b



  def run(args: List[String]): IO[ExitCode] = {
    IO {
      
      // println(Either.cond(false, "success value", new Throwable("an error occured")))

      // println(Either.fromOption(None, new Throwable("an error occured")))

      // println(integer1("34"))

      // println(add("34", "8"))

      val result1 = add("-8", "0").flatMap(it => Either.cond(it >= 0, it, new Throwable(s"$it is not positive")) )
      // println(result1)

      val result2 = result1
        .leftMap(th => th.getMessage())
        .fold(it  => "value has errors: " + it, it => "value was correct: " + it)
      println(result2)

      


    }.as(ExitCode.Success)
  }
}

