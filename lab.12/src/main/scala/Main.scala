import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import javax.sound.midi.Receiver
import scala.collection.SortedSet

case class Oblicz(n: Int)
case class Wynik(n: Int, fib: Int)
case class DoObliczenia(n1: Int, n2: Int)

class Boss extends Actor with ActorLogging {
  def receive: Receive = {
    case Oblicz(n) =>
      val nadzorca = context.actorOf(Props(Nadzorca()), "nadzorca")
      nadzorca ! Oblicz(n)
    case Wynik(n, fib) =>
      log.info(s"${fib}")
  }
}

class Nadzorca(cache: Map[Int, Int] = Map(1 -> 1, 2 -> 1), doZrobienia: List[ActorRef] = List()) extends Actor with ActorLogging {
  def receive: Receive = {
    case Oblicz(n) =>
      if (cache.get(n) == None) {
        val pracownik = context.actorOf(Props(Pracownik(n)), s"pracownik-${n}")
        pracownik ! Oblicz(n)
        context.become(noweWynikiWMapie(n, sender(), cache, pracownik :: doZrobienia))
      } else sender() ! Wynik(n, cache(n))
  }

  def noweWynikiWMapie(kluczRezultatu: Int, boss: ActorRef, cache: Map[Int, Int] = Map(1 -> 1, 2 -> 1), doZrobienia: List[ActorRef] = List()): Receive = {
    case DoObliczenia(n1, n2) =>
      if (cache.get(n1) == None) {
        val pracownik = context.actorOf(Props(Pracownik(n1)), s"pracownik-${n1}")
        pracownik ! Oblicz(n1)
        context.become(noweWynikiWMapie(kluczRezultatu, boss, cache, pracownik :: doZrobienia))
      } else sender() ! DoObliczenia(cache(n1), cache(n2))

    case Wynik(n, fib) =>
      if (n == kluczRezultatu) boss ! Wynik(n, fib)
      else {
        context.become(noweWynikiWMapie(kluczRezultatu, boss, cache + (n -> fib), doZrobienia.tail))
        doZrobienia(1) ! Oblicz(n + 1)
      } 
  }
}

class Pracownik(k: Int) extends Actor with ActorLogging {
  def receive: Receive = {
    case Oblicz(n) =>
      sender() ! DoObliczenia(n - 1, n - 2)
    case DoObliczenia(n1, n2) =>
      sender() ! Wynik(k, n1 + n2)
  }
}

@main def main: Unit = {
  val system = ActorSystem("Fibonacci")
  val boss = system.actorOf(Props[Boss](), "boss")
  boss ! Oblicz(5)
}


// GRACZ1:
// GRACZ1 GRAJ (gracz2, gracz3)
// gracz2 ! Graj(gracz3, gracz1)
// gracz1 przeciwnik: gracz2

// GRACZ2:
// GRACZ2 GRAJ (gracz3, gracz1)
// gracz3 ! Graj(gracz1, gracz2)
// gracz2 przeciwnik: gracz3

// GRACZ3:
// GRACZ3 GRAJ (gracz1, gracz2)
// gracz1 ! Graj(gracz2, gracz3)
// gracz3 przeciwnik: gracz1


