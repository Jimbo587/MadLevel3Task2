package com.example.madlevel3task2

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel3task2.databinding.FragmentStartBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding
    private var customTabHelper: CustomTabHelper = CustomTabHelper()

    private val portals = arrayListOf<Portal>()
    private val portalAdapter = PortalAdapter(portals) { portal: Portal ->
        portalClicked(
            portal
        )
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPortal.layoutManager =
            GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        binding.rvPortal.adapter = portalAdapter

        createItemTouchHelper().attachToRecyclerView(binding.rvPortal)
        observeAddReminderResult()
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                portals.removeAt(position)
                portalAdapter.notifyDataSetChanged()
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun observeAddReminderResult() {
        setFragmentResultListener(PORTAL) { _, bundle ->
            bundle.getParcelable<Portal>(PORTAL)?.let {
                portals.add(it)
            } ?: Log.e("Portal", "No Portal received!")
        }
    }

    private fun portalClicked(portal: Portal) {
        val url = portal.portalURL
        val builder = CustomTabsIntent.Builder()

        // modify toolbar color
        builder.setToolbarColor(ContextCompat.getColor(this.requireContext(), R.color.colorPrimary))

        // add share button to overflow menu
        builder.addDefaultShareMenuItem()

        val anotherCustomTab = CustomTabsIntent.Builder().build()

        val requestCode = 100
        val intent = anotherCustomTab.intent
        intent.data = Uri.parse(url)

        val pendingIntent = PendingIntent.getActivity(this.requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        builder.addMenuItem("Sample item", pendingIntent)

        // menu item icon
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        builder.setActionButton(bitmap, "Android", pendingIntent, true)

        // modify back button icon
        builder.setCloseButtonIcon(bitmap)

        // show website title
        builder.setShowTitle(true)

        // animation for enter and exit of tab
        builder.setStartAnimations(this.requireContext(), android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(this.requireContext(), android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = builder.build()

        // check is chrome available
        val packageName = customTabHelper.getPackageNameToUse(this.requireContext(), url)

        if (packageName == null) {
            // if chrome not available open in web view
            val intentOpenUri = Intent(this.requireContext(), WebView::class.java)
            startActivity(intentOpenUri)
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(this.requireContext(), Uri.parse(url))
        }
    }
}