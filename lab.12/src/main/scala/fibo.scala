package fibo

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import javax.sound.midi.Receiver
import scala.collection.SortedSet

case class Obliczam(n: Int, m: Int, cache: Map[Int, Int])
case class Oblicz(n: Int)
case class Wynik(n: Int, fib: Int)

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
      if (cache.contains(n)) {
        context.parent ! Wynik(n, cache(n))
      } else {
        val pracownik = context.actorOf(Props(Pracownik(n)))
        pracownik ! Obliczam(n-2, n-1, cache)
        context.become(obliczanie(n, sender(), cache, pracownik :: doZrobienia))
      }
  }

  def obliczanie(kluczRezultatu: Int, boss: ActorRef, cache: Map[Int, Int] = Map(1 -> 1, 2 -> 1), doZrobienia: List[ActorRef] = List()): Receive = {
    case Wynik(n, fib) =>   // Map(1 -> 1, 2 -> 1, 3 -> 2)
      if (n == kluczRezultatu) {
        boss ! Wynik(n, fib)
      } else {
        // log.info(s"${cache}")
        val maxymalnyWCache = cache.toList.maxBy((x, y) => x)._1
        val pracownik = context.actorOf(Props(Pracownik(maxymalnyWCache + 1)))
        pracownik ! Obliczam(maxymalnyWCache - 1, maxymalnyWCache, cache)
        context.become(obliczanie(kluczRezultatu, boss, cache + (n -> fib), pracownik :: doZrobienia))
      }
    case Obliczam(n, m, cache) => 
        val maxymalnyWCache = cache.toList.maxBy((x, y) => x)._1
        val pracownik = context.actorOf(Props(Pracownik(maxymalnyWCache + 1)))
        pracownik ! Obliczam(maxymalnyWCache - 1, maxymalnyWCache, cache)
        // context.become(obliczanie(kluczRezultatu, boss, cache, pracownik :: doZrobienia))
  }
}

class Pracownik(k: Int) extends Actor with ActorLogging { 
  def receive: Receive = {
    case Obliczam(minN, maxN, cache) =>
      if (cache.contains(maxN) == true) {
        sender() ! Wynik(maxN + 1, cache(maxN) + cache(minN))
      } else {
        sender() ! Obliczam(minN, maxN, cache)
      }
  }
}

@main def fibo: Unit = {
  val system = ActorSystem("Fibonacci")
  val boss = system.actorOf(Props[Boss](), "boss")
  boss ! Oblicz(6)
}