package com.neplace.neplacecustomer.inapppurchase

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.android.billingclient.api.*
import com.example.neplacecustomer.R
import com.example.neplacecustomer.inapppurchase.logger
import com.example.neplacecustomer.login.BaseActivity


class InappPurchaseScreen : BaseActivity() {
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_purchase)
        setTitle("In app purchase")

        button = findViewById(R.id.buttonBuyProduct)


        val skuList = ArrayList<String>()
        skuList.add("android.test.purchased")

        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                // To be implemented in a later section.
            }

        var billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        button.setOnClickListener {

            billingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingServiceDisconnected() {
                    val toast =
                        Toast.makeText(applicationContext, "Disconnected ", Toast.LENGTH_SHORT)
                    toast.show()
                }

                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    val toast =
                        Toast.makeText(applicationContext, "connecting ", Toast.LENGTH_SHORT)
                    toast.show()
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {


                        val params = SkuDetailsParams.newBuilder()
                        params.setSkusList(skuList)
                            .setType(BillingClient.SkuType.INAPP)

                        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->


                            for (skuDetails in skuDetailsList!!) {
                                val flowPurchase = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build()
                                logger(skuDetails.sku)
                                val responseCode = billingClient.launchBillingFlow(
                                    this@InappPurchaseScreen,
                                    flowPurchase
                                ).responseCode

                                logger("Code is " + responseCode)
                            }

                        }
                    }
                }

            })
        }


    }
}