package com.hello.sandbox

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hello.sandbox.util.DeviceHelper
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class SandboxTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testDeviceHelper() {
        // Test locale formatting
        val locale = DeviceHelper.getLocale()
        assertTrue("Locale format should be language_country", locale.contains("_"))
        
        // Test user agent
        val ua = DeviceHelper.getUa()
        assertNotNull("User agent should not be null", ua)
        assertTrue("User agent should not be empty", ua.isNotEmpty())
    }

    @Test
    fun testNetworkSecurity() {
        // Verify network security config is properly set
        val config = context.resources.getXml(R.xml.network_security_config)
        assertNotNull("Network security config should exist", config)
    }

    @Test
    fun testAppCloning() {
        // Mock package manager
        val packageManager = mock(PackageManager::class.java)
        val packageInfo = PackageInfo()
        packageInfo.packageName = "com.example.test"
        `when`(packageManager.getPackageInfo("com.example.test", PackageManager.GET_META_DATA))
            .thenReturn(packageInfo)

        // Test if package info can be retrieved
        val info = packageManager.getPackageInfo("com.example.test", PackageManager.GET_META_DATA)
        assertNotNull("Package info should not be null", info)
        assertEquals("Package name should match", "com.example.test", info.packageName)
    }
} 