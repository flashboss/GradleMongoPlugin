package com.sourcemuse.gradle.plugin.flapdoodle.adapters
import de.flapdoodle.embed.mongo.transitions.ImmutableMongodStarter
import de.flapdoodle.embed.mongo.packageresolver.Command
import de.flapdoodle.embed.process.net.HttpProxyFactory
import de.flapdoodle.embed.process.config.ImmutableDownloadConfig
import de.flapdoodle.embed.process.distribution.Distribution
import de.flapdoodle.embed.process.distribution.Version
import de.flapdoodle.embed.process.io.progress.ProgressListener
import de.flapdoodle.embed.process.runtime.CommandLinePostProcessor
import de.flapdoodle.reverse.transitions.Start

class CustomFlapdoodleRuntimeConfig extends ImmutableMongodStarter {
    private final Version version
    private final String mongodVerbosity
    private final String downloadUrl
    private final String proxyHost
    private final int proxyPort

    CustomFlapdoodleRuntimeConfig(Version version,
                                  String mongodVerbosity,
                                  String downloadUrl,
                                  String proxyHost,
                                  int proxyPort) {
        this.version = version
        this.mongodVerbosity = mongodVerbosity
        this.downloadUrl = downloadUrl
        this.proxyHost = proxyHost
        this.proxyPort = proxyPort
    }

    ImmutableMongodStarter.Builder defaults(Command command) {

        ImmutableDownloadConfig.Builder downloadConfigBuilder = ImmutableDownloadConfig.builder()
        Start.to(ProgressListener.class)
			.providedBy(new CustomFlapdoodleProcessLogger(version))

        if (downloadUrl) {
            downloadConfigBuilder.downloadPath(downloadUrl)
        }

        if (proxyHost) {
          downloadConfigBuilder.proxyFactory(new HttpProxyFactory(proxyHost, proxyPort))
        }

        runtimeConfigBuilder.commandLinePostProcessor(new CommandLinePostProcessor() {
            @Override
            List<String> process(Distribution distribution, List<String> args) {
                if (mongodVerbosity) args.add(mongodVerbosity)
                return args
            }
        })

        this.build()
    }
}
