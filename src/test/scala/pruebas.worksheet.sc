import kmedianas._
import org.scalameter._
import scala.collection.parallel.ParSeq


val standardConfig = config(
  Key.exec.minWarmupRuns := 20,
  Key.exec.maxWarmupRuns := 40,
  Key.exec.benchRuns := 25,
  Key.verbose := false
) withWarmer (Warmer.Default())

val numPuntos = 500000
val eta = 12
val k = 32

val puntos: Seq[Punto] = Seq(
  new Punto(1.0, 2.0, 3.0),
  new Punto(4.0, 5.0, 6.0),
  new Punto(7.0, 8.0, 9.0),
  new Punto(10.0, 11.0, 12.0),
  new Punto(13.0, 14.0, 15.0)
)

val medianas: Seq[Punto] = Seq(
  new Punto(2.0, 3.0, 4.0),
  new Punto(5.0, 6.0, 7.0),
  new Punto(8.0, 9.0, 10.0)
)

val puntosPar: ParSeq[Punto] = ParSeq(
  new Punto(1.0, 2.0, 3.0),
  new Punto(4.0, 5.0, 6.0),
  new Punto(7.0, 8.0, 9.0),
  new Punto(10.0, 11.0, 12.0),
  new Punto(13.0, 14.0, 15.0)
)

val medianasPar: ParSeq[Punto] = ParSeq(
  new Punto(2.0, 3.0, 4.0),
  new Punto(5.0, 6.0, 7.0),
  new Punto(8.0, 9.0, 10.0)
)

val clasificaSeq = clasificarSeq(puntos, medianas)
val clasificaPar = clasificarPar(puntosPar, medianasPar)

val mediaActualizadaSeq = actualizarSeq(clasificaSeq, medianas)
val mediaActualizadaPar = actualizarPar(clasificaPar, medianasPar)
 
val convergenciaSeq = hayConvergenciaSeq(eta, medianas, mediaActualizadaSeq)
val convergenciaPar = hayConvergenciaPar(eta, medianasPar, mediaActualizadaPar)
/*
val puntosSeq = generarPuntosSeq(k, numPuntos)
val medianasSeq = inicializarMedianasSeq(k, puntosSeq)

val puntosPar = generarPuntosPar(k, numPuntos)
val medianasPar = inicializarMedianasPar(k, puntosPar)

println("hola")

val tiempoSeq = standardConfig measure {
  kMedianasSeq(puntosSeq, medianasSeq, eta)
}

val tiempoPar = standardConfig measure {
  kMedianasPar(puntosPar, medianasPar, eta)
}
*/