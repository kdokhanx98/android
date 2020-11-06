FROM ubuntu:18.04

# Install Android Studio requirements
# See https://developer.android.com/studio/install#linux
RUN apt-get update -y \
&& apt-get install -y --no-install-recommends \
wget \
curl \
unzip \
openjdk-11-jre \
file \
build-essential \
&& rm -rf /var/lib/apt/lists/*

ENV ANDROID_SDK_ROOT /android-sdk

WORKDIR /android-sdk
RUN wget -q https://dl.google.com/android/repository/commandlinetools-linux-6200805_latest.zip && \
  unzip commandlinetools-linux-6200805_latest.zip && \
  rm commandlinetools-linux-6200805_latest.zip

RUN yes | ${ANDROID_SDK_ROOT}/tools/bin/sdkmanager --sdk_root=${ANDROID_SDK_ROOT} --licenses

ENV PATH ${PATH}:/android-sdk/tools/bin

# Install NDK manually. Other SDK parts are installed automatically by gradle.
RUN sdkmanager --sdk_root=${ANDROID_SDK_ROOT} ndk-bundle

ENV ANDROID_NDK_ROOT ${ANDROID_SDK_ROOT}/ndk-bundle
ENV PATH ${PATH}:${ANDROID_NDK_ROOT}/toolchains/llvm/prebuilt/linux-x86_64/bin/:${ANDROID_NDK_ROOT}

RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y --default-toolchain none
ENV PATH ${PATH}:/root/.cargo/bin
