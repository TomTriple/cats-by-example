import cats.effect.IOApp
import cats.effect.IO
import cats.effect.ExitCode
import cats.implicits._
import scala.util.Try
import cats.data.Validated
import cats.Applicative
import cats.Parallel
import cats.data.NonEmptyList
import cats.data.Validated.Invalid
import cats.data.Validated.Valid

object MainTutValidated extends IOApp {

  case class Person(name:String, age:Int)
  
  type AppResult1[A] = Either[List[String], A]

  def validateUsername1(str:String):AppResult1[String] = 
    Either.cond(str == "myUsername", str, List(s"username ist not correct"))

  def validateAge1(str:String):AppResult1[Int] = 
    Either.catchNonFatal(str.toInt).leftMap(_ => List("age is not an integer"))

  type AppResult2[A] = Validated[List[String], A]

  def validateUsername2(str:String):AppResult2[String] = 
    Validated.cond(str == "myUsername", str, List(s"username ist not correct"))

  def validateAge2(str:String):AppResult2[Int] = 
    Validated.catchNonFatal(str.toInt).leftMap(_ => List("age is not an integer"))    


  def run(args: List[String]): IO[ExitCode] = {
    IO {

      val result1 = for {
        name <- validateUsername1("wrongUsername")
        age  <- validateAge1("notAnInteger")
      } yield Person(name, age)
      // stops on the first .left
      // println(result1)

/* // does not compile as an applicative does not support .flatMap
      val result2 = for {
        name <- validateUsername2("wrongUsername")
        age  <- validateAge2("notAnInt")
      } yield Person(name, age)
      println(result2)
*/
      // ? comes from the kind projector plugin
      val result3 = Applicative[Validated[List[String], ?]].product(validateUsername2("wrongUsername"), validateAge2("notAnInt")).map(Person.tupled)
      // println(result3)

      // same as above
      val result4 = Applicative[Validated[List[String], ?]].map2(validateUsername2("wrongUsername"), validateAge2("notAnInt"))(Person.apply)
      // println(result4)

      // 1. short version - uses implicits
      val result5 = (validateUsername2("wrongUsername"), validateAge2("notAnInt")).mapN(Person.apply)
      // println(result5)
 
      // 2. short version - uses implicits 
      val result6 = (validateUsername2("wrongUsername") |@| validateAge2("notAnInt")).map(Person.apply)
      // println(result6) 

      // you can stay with existing eithers as they can be converted with .toValidated 
      val result7 = (validateUsername1("wrongUsername").toValidated, validateAge1("notAnInt").toValidated).mapN(Person.apply).toEither
      // println(result7) 

      // syntax sugar for above version - uses the typeclass Parallel, which "links" an monad with an applicative
      // result: you get a monad that supports parallel composition also 
      val result8 = (validateUsername1("wrongUsername"), validateAge1("notAnInt")).parMapN(Person.apply)
      println(result8) 

    }.as(ExitCode.Success) 
  }
}

