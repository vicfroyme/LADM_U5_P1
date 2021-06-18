package mx.tecnm.tepic.ladm_u5_p21

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()
    var posicion = ArrayList<Data>()
    lateinit var location : LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }

        baseRemota.collection("tecnologico")
                .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                    if(firebaseFirestoreException!=null){
                        textView.setText("ERROR: "+firebaseFirestoreException.message)
                        return@addSnapshotListener
                    }
                    var resultado = ""  
                    posicion.clear()
                    for (document in querySnapshot!!){
                        var data = Data()
                        data.nombre = document.getString("nombre").toString()
                        data.pos1 = document.getGeoPoint("posicion1")!!
                        data.pos2 = document.getGeoPoint("posicion2")!!

                        resultado += data.toString()+"\n\n"
                        posicion.add(data)
                    }
                    textView.setText(resultado)
                }

        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var oyente = oyente2(this)
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 01f, oyente)

    }

}

class oyente2 (puntero:MainActivity): LocationListener {
    var p = puntero

    override fun onLocationChanged(location: Location) {
        p.textView2.setText("Coordenadas: ${location.latitude},${location.latitude}")
        var geoPosicionGPS = GeoPoint(location.latitude, location.longitude)
        for (item in p.posicion) {
            if (item.estoyEn(geoPosicionGPS)) {
                p.textView3.setText("ESTÁS EN: ${item.nombre}")
            }

        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}