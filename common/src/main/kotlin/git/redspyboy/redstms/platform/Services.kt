package git.redspyboy.redstms.platform

import git.redspyboy.redstms.Constants
import git.redspyboy.redstms.platform.services.PlatformHelper
import java.util.ServiceLoader

object Services {
    val PLATFORM = load(PlatformHelper::class.java)

    fun <T> load(clazz: Class<T>): T {
        val loadedService = ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow {
                IllegalStateException("Failed to load service for ${clazz.name}")
            }
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz)
        return loadedService
    }
}