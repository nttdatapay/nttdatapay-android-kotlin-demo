package com.example.demoaipaysdk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.demoaipaysdk.databinding.ActivityMainBinding
import com.ntt.ndpsaipaycheckout.PayActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Objects
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setSupportActionBar(binding!!.toolbar)

        binding!!.fab.setOnClickListener { view: View? ->
            val txnId = UUID.randomUUID().toString().replace("-", "").substring(0, 10)
            Toast.makeText(this@MainActivity, "Initiating NDPS transaction", Toast.LENGTH_SHORT)
                .show()
            val newPayIntent = Intent(this@MainActivity, PayActivity::class.java)

            newPayIntent.putExtra("merchantId", "317157")
            newPayIntent.putExtra("password", "Test@123")
            newPayIntent.putExtra("prodid", "NSE")
            newPayIntent.putExtra("signature_request", "KEY1234567234")
            newPayIntent.putExtra("signature_response", "KEYRESP123657234")
            newPayIntent.putExtra("enc_request", "A4476C2062FFA58980DC8F79EB6A799E")
            newPayIntent.putExtra("salt_request", "A4476C2062FFA58980DC8F79EB6A799E")
            newPayIntent.putExtra("salt_response", "75AEF0FA1B94B3C10D4F5B268F757F11")
            newPayIntent.putExtra("enc_response", "75AEF0FA1B94B3C10D4F5B268F757F11")
            newPayIntent.putExtra("txncurr", "INR")
            newPayIntent.putExtra("custacc", "100000036600")
            newPayIntent.putExtra("amt", "2.00")
            newPayIntent.putExtra("txnid", txnId)
            //            newPayIntent.putExtra("multi_products", createMultiProductData());
            newPayIntent.putExtra("isLive", false) //false true
            newPayIntent.putExtra("custFirstName", "test user")
            newPayIntent.putExtra("customerEmailID", "testuser@xyz.in")
            newPayIntent.putExtra("customerMobileNo", "8888888811")
            newPayIntent.putExtra("udf1", "udf1")
            newPayIntent.putExtra("udf2", "udf2")
            newPayIntent.putExtra("udf3", "udf3")
            newPayIntent.putExtra("udf4", "udf4")
            newPayIntent.putExtra("udf5", "udf5")
            newPayIntent.putExtra(
                "subChannel",
                "NB"
            ) //To enable specific payment mode use this parameter else comment itout
            initNDPSPayments.launch(newPayIntent)
        }
    }

    var initNDPSPayments: ActivityResultLauncher<Intent> =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? -> }

    fun createMultiProductData(): String {
        val jsonArray = JSONArray()
        val jsonObjOne = JSONObject()
        val jsonObjTwo = JSONObject()
        try {
            jsonObjOne.put("prodName", "NSE")
            jsonObjOne.put("prodAmount", 1.00)
            jsonObjTwo.put("prodName", "AIPAY")
            jsonObjTwo.put("prodAmount", 1.00)
            jsonArray.put(jsonObjOne)
            jsonArray.put(jsonObjTwo)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        println("jsonArray from createMultiProductData = $jsonArray")
        return jsonArray.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("resultCode = $resultCode")
        println("onActivityResult data = $data")

        if (data != null && resultCode != 2 && resultCode != 3) {
//            println("IsIntent = " + (Objects.requireNonNull(data.extras)?.getString("IsIntent") ?: ))
            println("ArrayList data = " + data.extras!!.getString("response"))
            Toast.makeText(this, data.extras!!.getString("response"), Toast.LENGTH_LONG).show()
            if (resultCode == 1) {
                Toast.makeText(
                    this@MainActivity, "Transaction Successful! \n" + data.extras!!
                        .getString("response"), Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity, "Transaction Failed! \n" + data.extras!!
                        .getString("response"), Toast.LENGTH_LONG
                ).show()
            }
        } else {
            if (resultCode == 2) {
                Toast.makeText(this@MainActivity, "Transaction Cancelled!", Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(this@MainActivity, "Transaction Timeout!", Toast.LENGTH_LONG).show()
            }
        }
    } //onActivityResult
}