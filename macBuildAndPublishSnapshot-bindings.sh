set -e
#!/bin/sh
./gradlew multiplatform-crypto-libsodium-bindings:publishMacosX64PublicationToSnapshotRepository

./gradlew multiplatform-crypto-libsodium-bindings:publishIosArm32PublicationToSnapshotRepository \
multiplatform-crypto-libsodium-bindings:publishIosArm64PublicationToSnapshotRepository \
multiplatform-crypto-libsodium-bindings:publishIosX64PublicationToSnapshotRepository

./gradlew multiplatform-crypto-libsodium-bindings:publishWatchosArm32PublicationToSnapshotRepository \
multiplatform-crypto-libsodium-bindings:publishWatchosArm64PublicationToSnapshotRepository \
multiplatform-crypto-libsodium-bindings:publishWatchosX86PublicationToSnapshotRepository

./gradlew multiplatform-crypto-libsodium-bindings:publishTvosArm64PublicationToSnapshotRepository \
multiplatform-crypto-libsodium-bindings:publishTvosX64PublicationToSnapshotRepository
set +e