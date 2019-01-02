package org.wit.archaeologicalfieldwork.views.hillfortList

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Checkable
import android.widget.ImageButton
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.R.id.item_settings
import org.wit.archaeologicalfieldwork.activities.SettingsActivity
import org.wit.archaeologicalfieldwork.adapters.HillfortAdapter
import org.wit.archaeologicalfieldwork.adapters.HillfortListener
import org.wit.archaeologicalfieldwork.models.Favourite
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.views.BaseView

class HillfortListView : BaseView(), HillfortListener {

    lateinit var presenter: HillfortListPresenter


    override fun onHillfortClick(hillfort: Hillfort) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        init(toolbarMain)

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadHillforts()
    }

    override fun showHillforts(hillforts: List<Hillfort>){
        val listener = this
        async(UI){
            info("about to show ${hillforts.size} hillforts.")
            //images might not load instantly, but this is fine since it might take a while to load them and the app is usable until then.
            recyclerView.adapter = HillfortAdapter(hillforts, presenter.app.images.findAll(), presenter.app.ratings.findAll(), presenter.app.favourites.findAll(), listener)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort_list, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.app_bar_search)?.actionView as SearchView).apply{
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_map -> presenter.doShowHillfortsMap()
            R.id.item_settings -> presenter.doViewSettings()
            R.id.filterFavourites -> presenter.doFilterFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadHillforts()
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onFavouriteClick(hillfort: Hillfort, favouriteButton: ImageButton) {
        presenter.doSetAsFavourite(hillfort, favouriteButton)
    }
}