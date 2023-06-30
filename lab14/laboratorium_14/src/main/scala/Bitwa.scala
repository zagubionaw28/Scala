package jp1.akka.lab14

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.actor.Props
import akka.actor.ActorSystem
import scala.concurrent.duration.*
import javax.sound.midi.Receiver

import akka.actor.ActorRef
import java.util.Random

case object StrzałzŁuku
case object Strzał
case class Strzelaj(przeciwnik: ActorRef)
case class Mess(siłaWyższa: ActorRef)
case class Przeciwnik(przeciwnik: ActorRef)
case class JestemGotówNaOstrzał(zamek: ActorRef)
case object ProdukcjaObrońców
case class PrzeciwnikIJa(zamki: List[ActorRef])
case object Muerto

class Zamek extends Actor with ActorLogging {
  def receive: Receive = {
    case Przeciwnik(przeciwnik) =>
      context.become(tworzenieObrońców(List(), 1, przeciwnik))
    case Mess(siłaWyższa) =>
      siłaWyższa ! JestemGotówNaOstrzał(self)
  }
  def tworzenieObrońców(listaAktywnychObrońców: List[ActorRef], n: Int, przeciwnik: ActorRef): Receive = {
    case Strzał =>
      log.info(s"dostalem strzal") 
      if (listaAktywnychObrońców.length == 0){
        self ! ProdukcjaObrońców
      }
    case ProdukcjaObrońców =>
      if (listaAktywnychObrońców.length == 100) {
        // log.info(s"${n}\n${listaAktywnychObrońców.mkString("\n")}") // opcjonalne
        listaAktywnychObrońców.foreach((x) => x ! Strzelaj(przeciwnik))
        context.become(bitwa(listaAktywnychObrońców, przeciwnik))
      } else {
        val aktywnyObrońca = context.actorOf(Props[Obrońca](), s"aktywnyObronca${n}")
        // log.info(s"$aktywnyObrońca")
        context.become(tworzenieObrońców(aktywnyObrońca :: listaAktywnychObrońców, n + 1, przeciwnik))
        self ! ProdukcjaObrońców
      }
  }

  def bitwa(listaAktywnychObrońców: List[ActorRef], przeciwnik: ActorRef): Receive = {
    case Strzał =>
      listaAktywnychObrońców.foreach((x) => x ! Strzelaj(przeciwnik))
    case StrzałzŁuku =>
      log.info(s" ---- ${listaAktywnychObrońców.length}")
      if (listaAktywnychObrońców.length == 0) {
        log.info("Przegrałem!!!!!!!!!!!!!!!!!!!!!!!!!!")
        context.system.terminate()
        context.become(nicość)
      } else {
        // log.info("-----------------------------------------------")
        // log.info(s"${listaAktywnychObrońców.mkString("\n")}")
        if (Random().nextInt(2) == 1) {
          val umierający = listaAktywnychObrońców(Random().nextInt(listaAktywnychObrońców.length))
          // log.info(s"${umierający} XXXXXXXXXXXXXXXXXXXXXXXXXX")
          val nowaLista = listaAktywnychObrońców.filter((x) => x != umierający)

          context.become(bitwa(nowaLista, przeciwnik))
          umierający ! Muerto
        }
      }
  }

  def nicość: Receive = {
    case _ =>
  }
}

class Obrońca extends Actor with ActorLogging {
  def receive: Receive = {
    case Strzelaj(przeciwnik) =>
      // log.info("Strzelam ===============================")
      przeciwnik ! StrzałzŁuku
    case Muerto => log.info("I'm dead")
      context.become(umarłem)
  }

  def umarłem: Receive = {
    case _ =>
  }
}

class SiłaWyższa extends  Actor with ActorLogging {
  def receive: Receive = {
    case JestemGotówNaOstrzał(zamek) =>
      context.become(strzelamDo(zamek :: List()))
  }

  def strzelamDo(zamki: List[ActorRef]): Receive = {
    case JestemGotówNaOstrzał(zamek) =>
      context.become(strzelamDo(zamek :: zamki))
    case Strzał =>
      context.actorSelection("../Zamek*") ! Strzał
  }
}

@main def go: Unit = {
  val system = ActorSystem("system")
  // żeby planista mógł działać „w tle” potrzebuje puli wątków:
  implicit val executionContext = system.dispatcher

  // tworzymy zamki
  val zamekA = system.actorOf(Props[Zamek](), "ZamekA")
  val zamekB = system.actorOf(Props[Zamek](), "ZamekB")
  val siłaWyższa = system.actorOf(Props[SiłaWyższa](), "silaWyzsza")
  
  zamekA ! Mess(siłaWyższa)
  zamekB ! Mess(siłaWyższa)
  zamekA ! Przeciwnik(zamekB)
  zamekB ! Przeciwnik(zamekA)
  // siłaWyższa ! PrzeciwnikIJa(List(zamekA, zamekB))
  // wczytujemy konfigurację aplikacji z „resources/application.conf”
  // i odwołujemy się do jej elementu, wyrażonego w milisekundach
  val config = system.settings.config
  val delay = config.getInt("planista.delay").milli

  // uruchamiamy planistę
  system.scheduler.scheduleWithFixedDelay(
    delay,
    delay,
    siłaWyższa,
    Strzał
  )

  // Uwaga! Oczywiście powyższy planista kontaktuje się jedynie
  // z jednym z zamków!!!
}
