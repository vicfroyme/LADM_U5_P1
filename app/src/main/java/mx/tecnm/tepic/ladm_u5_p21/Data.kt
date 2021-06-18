package mx.tecnm.tepic.ladm_u5_p21

import com.google.firebase.firestore.GeoPoint

class Data {
    var nombre : String = ""
    var pos1 : GeoPoint= GeoPoint(0.0,0.0)
    var pos2 : GeoPoint=GeoPoint(0.0,0.0)

    override fun toString(): String {
        return nombre+"\n"+pos1.latitude+","+pos1.longitude+"\n"+
                pos2.latitude+","+pos2.longitude
    }
    fun estoyEn(posicionActual:GeoPoint):Boolean{
        if (posicionActual.latitude>=pos1.latitude && posicionActual.longitude<=pos2.latitude){
            if(invertir(posicionActual.longitude)>=invertir(pos1.longitude) && invertir(posicionActual.longitude)<=invertir(pos2.longitude)){
                return true
            }
        }
        return false
    }
    private fun invertir(valor:Double):Double{
        return valor*-1
    }
}