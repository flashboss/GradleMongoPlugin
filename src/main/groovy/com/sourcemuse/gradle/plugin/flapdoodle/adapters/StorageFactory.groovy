package com.sourcemuse.gradle.plugin.flapdoodle.adapters

import com.sourcemuse.gradle.plugin.GradleMongoPluginExtension
import de.flapdoodle.embed.mongo.config.Storage

class StorageFactory {
    Storage getStorage(GradleMongoPluginExtension extension) {
        if(extension.storageLocation){
            Storage.of(extension.storageLocation, 0)
        } else {
            Storage.builder().build()
        }
    }
}
