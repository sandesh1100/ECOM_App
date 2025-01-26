package com.acodingsandesh.ecomapp

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acodingsandesh.ecomapp.adapters.MainImageAdapter
import com.acodingsandesh.ecomapp.view_models.ProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

/*
* Author - Sandesh Bais
* Date - 25th Jan, 2025
*/

class MainActivity : AppCompatActivity(), OnClickListener {
    private val TAG = MainActivity::class.java.name
    private lateinit var tvResponse: TextView
    private val viewModel: ProductViewModel by viewModels()
    private var mainImagesRV: RecyclerView? = null
    private var mainImagesAdapter: MainImageAdapter? = null
    private var tvTitle: TextView? = null
    private var tvBrandName: TextView? = null
    private var tvPrice: TextView? = null
    private var tvSubTitle: TextView? = null
    private var tvSKU: TextView? = null
    private var tvDescription: TextView? = null
    private var tvCnt: TextView? = null
    private var btnPlus: Button? = null
    private var btnMinus: Button? = null
    private var btnAddToBag: Button? = null
    private var btnShare: Button? = null
    private var productCnt = 0
    private var ivExitApp: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.productVM = viewModel
        binding.lifecycleOwner = this*/ //getting issue due to binding so for now switched to traditional way

        //init view
        tvResponse = findViewById(R.id.tv_response)
        tvTitle = findViewById(R.id.tv_title)
        tvBrandName = findViewById(R.id.brand_name)
        tvPrice = findViewById(R.id.final_price)
        tvSubTitle = findViewById(R.id.tv_sub_title)
        tvSKU = findViewById(R.id.tv_sku)
        tvDescription = findViewById(R.id.tv_description)
        tvCnt = findViewById(R.id.tvCounter)
        btnPlus = findViewById(R.id.btnPlus)
        btnPlus?.setOnClickListener(this)
        btnMinus = findViewById(R.id.btnMinus)
        btnMinus?.setOnClickListener(this)
        btnAddToBag = findViewById(R.id.btn_add)
        btnAddToBag?.setOnClickListener(this)
        btnShare = findViewById(R.id.btn_share)
        btnShare?.setOnClickListener(this)
        ivExitApp = findViewById(R.id.iv_back)
        ivExitApp?.setOnClickListener(this)

        mainImagesRV = findViewById(R.id.recyclerView)
        mainImagesRV?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainImagesRV?.adapter = MainImageAdapter(ArrayList()) //to initialize the adapter with empty or no images or a loader
        /*mainImagesAdapter = MainImageAdapter(
            ArrayList(listOf(
                "https://klinq.com/media/catalog/product/cache/6ba02f1c1feeeb8ea72e23b04b2a55ca/8/8/8809579838296-1_mj8bpalcovgwf41a.jpg",
                "https://klinq.com/media/catalog/product/cache/6ba02f1c1feeeb8ea72e23b04b2a55ca/8/8/8809579838296-2_p4rhplawjxx6njgi.jpg"
            )))*/

        //setting all observables
        setObservables()

        //fetch product details
        CoroutineScope(Dispatchers.IO).launch{
            viewModel.getProduct("6701", "253620", "en", "KWD") //you can pass desired product
        }

    }

    private fun setObservables(){
        viewModel.imagesLiveData.observe(this, Observer { list ->
            mainImagesAdapter = MainImageAdapter(list)
            mainImagesRV?.adapter = mainImagesAdapter
            mainImagesAdapter!!.notifyDataSetChanged()
        })

        viewModel.titleLiveData.observe(this, Observer { title ->
            tvTitle?.text = title
            tvSubTitle?.text = title //as both of them having same value
        })

        viewModel.brandNameLiveData.observe(this, Observer { brand ->
            tvBrandName?.text = brand
        })

        viewModel.priceLiveData.observe(this, Observer { price ->
            val dNum = price.toDouble()
            val finalPrice = "%.2f".format(dNum) //show only two digits after decimal
            tvPrice?.text = "$finalPrice KWD"
        })

        viewModel.skuLiveData.observe( this, Observer { sku ->
            tvSKU?.text = "SKU: $sku"
        })

        viewModel.descriptionLiveData.observe(this, Observer { desc ->
            tvDescription?.text = Html.fromHtml(desc)
        })

        viewModel.responseLiveData.observe(this, Observer { response ->
            tvResponse.text = response.data.toString()
        })

        viewModel.errorMsg.observe(this, Observer { errorMsg ->
            tvResponse.text = errorMsg.toString()
        })
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.btnPlus -> {
                    if(productCnt < 0) {
                        productCnt = 0
                        tvCnt?.text = "${productCnt++}"
                    } else {
                        Log.d(TAG, "cnt: $productCnt")
                        productCnt++
                        tvCnt?.text = "${productCnt}"
                        Log.d(TAG, "cnt after: $productCnt")
                    }
                }

                R.id.btnMinus -> {
                    if(productCnt < 0) {
                        productCnt = 0
                        tvCnt?.text = "${productCnt}"
                    } else {
                        tvCnt?.text = "${productCnt--}"
                    }
                }

                R.id.btn_add -> {
                    if(productCnt > 0){
                        Toast.makeText(this@MainActivity, "Total of $productCnt ${tvBrandName?.text} added to the bag!", Toast.LENGTH_SHORT).show()
                        productCnt = 0
                        tvCnt?.text = "$productCnt"
                    } else {
                        Toast.makeText(this@MainActivity, "Select how many you like to add", Toast.LENGTH_SHORT).show()
                        productCnt = 0
                        tvCnt?.text = "$productCnt"
                    }
                }

                R.id.iv_back -> {
                    Toast.makeText(this@MainActivity, "See you soon!", Toast.LENGTH_SHORT).show()
                    finishAndRemoveTask()
                }
            }
        }
    }
}