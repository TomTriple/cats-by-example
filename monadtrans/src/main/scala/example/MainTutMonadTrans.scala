import cats.effect.IOApp
import cats.effect.IO
import cats.effect.ExitCode
import cats.implicits._
import scala.util.Try
import cats.Functor
import cats.data.OptionT
import cats.data.Ior
import java.beans.PersistenceDelegate
import cats.data.IorT

object MainTutMonadTrans extends IOApp {

  /*
  
    Monad Transformers (e.g. OptionT, EitherT, IorT ...)

    – Transformers complement their corresponding (monadic) data types 
    – Contrary to other types monads do not compose 
    – You can build entire monad transformer stacks

  */ 

  case class User(username:String, age:Int)

  def run(args: List[String]): IO[ExitCode] = {
    IO {

      // Option Functor
 
      val optionOption = Option(Option(User("username", 77)))

      // It's better to use a composed functor instead of mapping twice 
      val result1 = optionOption.map(option => option.map(_.username))

      // Compose two functors and then map 
      val result2 = Functor[Option].compose[Option].map(optionOption)(_.username)


      // Either Functor - same as above, note the use of the kind projector ("?" syntax!)

      val eitherOption1 = Either.right[Throwable, Option[User]](Option(User("username1", 77)))

      val result3 = eitherOption1.map(option => option.map(_.username))

      val result4 = Functor[Either[Throwable, ?]].compose[Option].map(eitherOption1)(_.username)

      // Monad transformers

      val eitherOption2 = Either.right[Throwable, Option[User]](Option(User("username2", 23)))

      val result5 = for {
        aa <- eitherOption1
        bb <- eitherOption2
      } yield for {
        a <- aa
        b <- bb
      } yield a.age + b.age

      // Use a monad transformer instead of nested for comprehensions
      val result6:OptionT[Either[Throwable, ?], Int] = for {
        // a <- OptionT(eitherOption1)
        // a <- OptionT(Either.left[Throwable, Option[User]](new Throwable("error")))
        a <- OptionT(Either.right[Throwable,Option[User]](None))
        b <- OptionT(eitherOption2)
        _ = println("test")
      } yield a.age + b.age
      //println(result6)

      // result6.isDefined
      // eitherOption1.isDefined

      // result6.value
      

      case class TestResult(value:Int)
      case class Permission(str:String)

      val ior = Ior.right[TestResult, Permission](Permission("update-action"))

      IorT(OptionT.liftF(Either.right(ior))).flatMap()

    }.as(ExitCode.Success)
  }
}

