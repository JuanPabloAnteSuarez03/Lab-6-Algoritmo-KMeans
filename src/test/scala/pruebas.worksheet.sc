import kmedianas._
import org.scalameter._
import scala.collection.parallel.ParSeq


val standardConfig = config(
  Key.exec.minWarmupRuns := 20,
  Key.exec.maxWarmupRuns := 40,
  Key.exec.benchRuns := 25,
  Key.verbose := false
) withWarmer (Warmer.Default())

val numPuntos = 100
val eta = 0.01
val k = 64

val puntosSeq = generarPuntosSeq(k, numPuntos)
val medianasSeq = inicializarMedianasSeq(k, puntosSeq)

val puntosPar2 = generarPuntosPar(k, numPuntos)
val medianasPar2 = inicializarMedianasPar(k, puntosPar2)

val tiempoSeq = standardConfig measure {
  kMedianasSeq(puntosSeq, medianasSeq, eta)
}

val tiempoPar = standardConfig measure {
  kMedianasPar(puntosPar2, medianasPar2, eta)
}


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

val kMedianasSecuencial = kMedianasSeq(puntos, medianas, eta)
val kMedianasParalelo = kMedianasPar(puntosPar, medianasPar, eta)

//Casos Pruebas   
//clasificarPar
val puntos1Clasificacion = ParSeq(new Punto(1.0, 2.0, 3.0), new Punto(4.0, 5.0, 6.0))
val medianas1Clasificacion = ParSeq(new Punto(0.0, 0.0, 0.0))
val resultado1Clasificacion = clasificarPar(puntos1Clasificacion, medianas1Clasificacion)
// Se espera que todos los puntos sean clasificados bajo la única mediana

val puntos2Clasificacion = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianas2Clasificacion = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(4.0, 4.0, 4.0)
)
val resultado2Clasificacion = clasificarPar(puntos2Clasificacion, medianas2Clasificacion)
// Se espera que los puntos sean clasificados bajo las medianas correspondientes

val puntos3Clasificacion = ParSeq(
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0),
  new Punto(4.0, 4.0, 4.0)
)
val medianas3Clasificacion = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(4.0, 4.0, 4.0)
)
val resultado3Clasificacion = clasificarPar(puntos3Clasificacion, medianas3Clasificacion)
// Se espera que los puntos sean clasificados bajo las medianas correspondientes

val puntos4Clasificacion = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0),
  new Punto(4.0, 4.0, 4.0),
  new Punto(5.0, 5.0, 5.0)
)
val medianas4Clasificacion = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(3.0, 3.0, 3.0),
  new Punto(6.0, 6.0, 6.0)
)
val resultado4Clasificacion = clasificarPar(puntos4Clasificacion, medianas4Clasificacion)
// Se espera que los puntos sean clasificados correctamente

//actualizarPar
val puntos1actualizar = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianas1actualizar = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(4.0, 4.0, 4.0)
)
val clasificacion1actualizarr = clasificarPar(puntos1actualizar, medianas1actualizar)
val mediaActualizadaPar1 = actualizarPar(clasificacion1actualizarr, medianas1actualizar)
// Se espera que las medianas se actualicen con el promedio de los puntos correspondientes

val puntos2actualizar = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0),
  new Punto(4.0, 4.0, 4.0)
)
val medianas2actualizar = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(3.0, 3.0, 3.0),
  new Punto(6.0, 6.0, 6.0)
)
val clasificacion2actualizar = clasificarPar(puntos2actualizar, medianas2actualizar)
val mediaActualizadaPar2 = actualizarPar(clasificacion2actualizar, medianas2actualizar)
// Se espera que las medianas se actualicen con el promedio de los puntos correspondientes,
// y las medianas sin puntos asociados se mantengan iguales

val puntos3actualizar = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianas3actualizar = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(4.0, 4.0, 4.0),
  new Punto(6.0, 6.0, 6.0)
)
val clasificacion3actualizar = clasificarPar(puntos3actualizar, medianas3actualizar)
val mediaActualizadaPar3 = actualizarPar(clasificacion3actualizar, medianas3actualizar)
// Se espera que las medianas sin puntos asociados se mantengan iguales

val puntos4actualizar = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianas4actualizar = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(4.0, 4.0, 4.0)
)
val clasificacion4actualizar = clasificarPar(puntos4actualizar, medianas4actualizar)
val mediaActualizadaPar4 = actualizarPar(clasificacion4actualizar, medianas4actualizar)
// Se espera que las medianas sin puntos asociados se mantengan iguales

val puntos5actualizar = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianas5actualizar = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(2.0, 2.0, 2.0)
)
val clasificacion5actualizar = clasificarPar(puntos5actualizar, medianas5actualizar)
val mediaActualizadaPar5 = actualizarPar(clasificacion5actualizar, medianas5actualizar)
// Se espera que las medianas duplicadas se actualicen con el promedio de los puntos correspondientes

//hayConvergenciaPar()
val eta1convergencia = 0.1
val medianasViejas1convergencia = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianasNuevas1convergencia = ParSeq(
  new Punto(1.05, 1.05, 1.05),
  new Punto(2.05, 2.05, 2.05),
  new Punto(3.05, 3.05, 3.05)
)
val convergencia1 = hayConvergenciaPar(eta1convergencia, medianasViejas1convergencia, medianasNuevas1convergencia)
// Se espera que la función devuelva true ya que todas las distancias al cuadrado son menores o iguales a eta

val eta2convergencia = 0.1
val medianasViejas2convergencia = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianasNuevas2convergencia = ParSeq(
  new Punto(1.2, 1.2, 1.2),
  new Punto(2.2, 2.2, 2.2),
  new Punto(3.2, 3.2, 3.2)
)
val convergencia2 = hayConvergenciaPar(eta2convergencia, medianasViejas2convergencia, medianasNuevas2convergencia)
// Se espera que la función devuelva false ya que una o más distancias al cuadrado son mayores a eta

val eta3convergencia = 0.0
val medianasViejas3convergencia = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianasNuevas3convergencia = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val convergencia3 = hayConvergenciaPar(eta3convergencia, medianasViejas3convergencia, medianasNuevas3convergencia)
// Se espera que la función devuelva true ya que eta es cero y todas las distancias al cuadrado son iguales a cero

val eta4convergencia = -0.1
val medianasViejas4convergencia = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianasNuevas4convergencia = ParSeq(
  new Punto(1.05, 1.05, 1.05),
  new Punto(2.05, 2.05, 2.05),
  new Punto(3.05, 3.05, 3.05)
)
val convergencia4 = hayConvergenciaPar(eta4convergencia, medianasViejas4convergencia, medianasNuevas4convergencia)
// Se espera que la función devuelva false ya que eta negativo siempre será false

val eta5convergencia = 0.1
val medianasViejas5convergencia = ParSeq.empty[Punto]
val medianasNuevas5convergencia = ParSeq.empty[Punto]
val convergencia5 = hayConvergenciaPar(eta5convergencia, medianasViejas5convergencia, medianasNuevas5convergencia)
// Se espera que la función devuelva true ya que no hay medianas para evaluar y, por lo tanto, se considera que hay convergencia

//kMedianasPar
val puntos1k = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianas1k = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val eta1k = 0.1
val resultado1k = kMedianasPar(puntos1k, medianas1k, eta1k)
// Se espera que el resultado sea igual a las medianas iniciales, ya que ya se alcanzó la convergencia en la primera iteración

val puntos2k = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianas2k = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(0.0, 0.0, 0.0),
  new Punto(0.0, 0.0, 0.0)
)
val eta2k = 0.1
val resultado2k = kMedianasPar(puntos2k, medianas2k, eta2k)
// Se espera que el resultado sea diferente a las medianas iniciales, ya que se requieren iteraciones adicionales para alcanzar la convergencia

val puntos3k = ParSeq(
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0),
  new Punto(3.0, 3.0, 3.0)
)
val medianas3k = ParSeq(
  new Punto(0.0, 0.0, 0.0),
  new Punto(1.0, 1.0, 1.0),
  new Punto(2.0, 2.0, 2.0)
)
val eta3k = 0
val resultado3k = kMedianasPar(puntos3k, medianas3k, eta3k)
// Se espera que el resultado sea diferente a las medianas iniciales, ya que se requieren iteraciones adicionales para alcanzar la convergencia

val puntos4k = ParSeq.empty[Punto]
val medianas4k = ParSeq.empty[Punto]
val eta4k = 0.1
val resultado4k = kMedianasPar(puntos4k, medianas4k, eta4k)
// Se espera que el resultado sea una lista vacía, ya que no hay puntos ni medianas para calcular


