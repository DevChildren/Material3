import os
import sys
import subprocess
import shutil 

class UIColor:
    def __init__(self):
        self.setup_rich()

    def setup_rich(self):
        try:
            from rich.console import Console
            self.console = Console()
        except ImportError:
            print("[!] Paket 'rich' belum terinstal. Instalasi otomatis...")
            self.install_rich()
            from rich.console import Console
            self.console = Console()

    def install_rich(self):
        subprocess.run([sys.executable, "-m", "pip", "install", "rich"], check=True)

    def print_red(self, text):
        self.console.print(f"[bold red]{text}[/bold red]")

    def print_green(self, text):
        self.console.print(f"[green]{text}[/green]")

    def print_blue(self, text):
        self.console.print(f"[blue]{text}[/blue]")

class VirtualEnvManager:
    def __init__(self, venv_dir):
        self.venv_dir = venv_dir
        self.activate_script = f"bash -c 'source {venv_dir}/bin/activate && '"
        self.ui = None
        self.pip_path = os.path.join(self.venv_dir, "bin", "pip")
        self.python_path = os.path.join(self.venv_dir, "bin", "python")
        self.env_active = False
       
    def deactivate_venv(self):
       if "VIRTUAL_ENV" in os.environ:
        os.environ.pop("VIRTUAL_ENV")  # Hapus env var
        os.environ["PATH"] = os.environ["PATH"].replace(f"{self.venv_dir}/bin:", "")  # Hapus PATH venv
        print("[+] Virtual Environment dinonaktifkan.")
       else:
        print("[!] Virtual Environment tidak aktif.")

    def create_virtualenv(self):
        if not os.path.exists(self.venv_dir):
            print("[+] Membuat Virtual Environment...")
            subprocess.run(["python", "-m", "venv", self.venv_dir], check=True)
            print("[+] Virtual Environment berhasil dibuat.")

    def activate_virtualenv(self):
        """Aktifkan Virtual Environment tanpa keluar dari skrip."""
        if not self.env_active:
            os.environ["VIRTUAL_ENV"] = self.venv_dir
            os.environ["PATH"] = f"{self.venv_dir}/bin:" + os.environ["PATH"]
            print("[+] Virtual Environment telah diaktifkan dalam program.")
            self.env_active = True
   
    def activate_and_upgrade(self):
        """Mengaktifkan Virtual Environment dan upgrade pip"""
        print("[+] Mengaktifkan Virtual Environment dan upgrade pip...")
        
        venv_python = os.path.join(self.venv_dir, "bin", "python")  # Linux/macOS
        if not os.path.exists(venv_python):  # Windows
            venv_python = os.path.join(self.venv_dir, "Scripts", "python.exe")
        
        try:
            subprocess.run([venv_python, "-m", "pip", "install", "--upgrade", "pip"], check=True)
            self.setup_rich()
            self.print_success("Pip berhasil diperbarui")
            
        except subprocess.CalledProcessError as e:
            print(f"[!] Gagal upgrade pip: {e}")

    def install_package(self, package):
        subprocess.run([self.pip_path, "install", package], check=True)

    def install_dependencies(self):
        dependencies = ["rich", "requests"]
        for package in dependencies:
            print(f"[+] Menginstal {package}...")
            self.install_package(package)
    
    def setup_rich(self):
        """Mengimpor rich hanya setelah memastikan bahwa pustaka terinstal."""
        if self.ui is None:  # Hanya inisialisasi jika belum dibuat
            try:
                from rich.console import Console  # Impor ulang untuk jaga-jaga
                self.ui = UIColor()  # Pakai UIColor yang benar
            except ImportError:
                print("[!] Paket 'rich' belum terinstal. Instalasi otomatis...")
                self.install_package("rich")
                try:
                    from rich.console import Console
                    self.ui = UIColor()
                except ImportError:
                    print("[!] Gagal menginstal 'rich'. Pastikan koneksi internet tersedia.")
                    self.ui = None  # Hindari penggunaan jika gagal

    def print_success(self, text):
        """Pastikan rich sudah ada sebelum mencetak warna."""
        self.setup_rich()  # Pastikan `self.ui` sudah dibuat
        if self.ui:
            self.ui.print_green(text)
        else:
            print(text)  # Gunakan print biasa jika rich gagal dimuat

class AndroidSDKSetup:
    def __init__(self, sdk_dir):
        self.ui = UIColor()
        self.sdk_dir = sdk_dir
        self.sdk_url = "https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip"
        self.sdk_zip = os.path.join(os.path.expanduser("~"), "cmdline-tools-dev.zip")
        self.latest_dir = os.path.join(sdk_dir, "cmdline-tools/latest")

    def setup_sdk(self):
        self.ui.print_green("[+] Mengatur Android SDK...")
        os.makedirs(self.sdk_dir, exist_ok=True)
        
        if not os.path.exists(self.sdk_zip):
            self.download_sdk()
        self.extract_sdk()
        self.setup_latest_folder()

    def download_sdk(self):
        print("[+] Mengunduh cmdline-tools terbaru...")
        subprocess.run(["wget", self.sdk_url, "-O", self.sdk_zip], check=True)

    def extract_sdk(self):
        print("[+] Mengekstrak cmdline-tools...")
        subprocess.run(["unzip", "-o", self.sdk_zip, "-d", self.sdk_dir], check=True)

    def setup_latest_folder(self):
        cmdline_tools_path = os.path.join(self.sdk_dir, "cmdline-tools")
        self.latest_dir = os.path.join(cmdline_tools_path, "latest")
    
        # Jika latest sudah ada, hapus isinya
        if os.path.exists(self.latest_dir):
            shutil.rmtree(self.latest_dir)  # Hapus seluruh isi folder latest
    
        os.makedirs(self.latest_dir, exist_ok=True)
    
        # Pindahkan semua isi cmdline-tools ke dalam latest, kecuali latest sendiri
        for item in os.listdir(cmdline_tools_path):
            item_path = os.path.join(cmdline_tools_path, item)
            if item != "latest":
                shutil.move(item_path, self.latest_dir)  # Gunakan shutil.move()

        print("[✔] Struktur cmdline-tools sudah diperbarui.")

  

    def install_platform_tools(self):
        sdkmanager = os.path.join(self.latest_dir, "bin", "sdkmanager")
        print("[+] Menginstal platform-tools...")
        subprocess.run([sdkmanager, "--install", "platform-tools"], check=True)

class PackageInstaller:
    def __init__(self, packages):
        self.packages = packages

    def install(self):
        print("[+] Memeriksa dan menginstal paket yang diperlukan...")
        subprocess.run(["pkg", "update", "-y"], check=True)
        subprocess.run(["pkg", "upgrade", "-y"], check=True)
        subprocess.run(["pkg", "install", "-y"] + self.packages, check=True)

class BashrcConfig:
    def __init__(self, venv_dir, sdk_dir):
        self.bashrc_path = os.path.expanduser("~/.bashrc")
        self.venv_dir = venv_dir
        self.sdk_dir = sdk_dir
        self.config = f"""
# Konfigurasi Environment Termux
export PATH=$PATH:{sdk_dir}/platform-tools
export PATH=$PATH:/data/data/com.termux/files/usr/bin/
export ANDROID_SDK_ROOT=$HOME/{sdk_dir}
export PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH


alias open='mv /data/data/com.termux/files/home/my-android-app/app/build/outputs/apk/debug/app-debug.apk /sdcard/develite/ && am start -a android.intent.action.VIEW -d file:///sdcard/develite/app-debug.apk -t application/vnd.android.package-archive'

# Auto-activate Virtual Environment
if [ -d "$venv_dir" ]; then
    source "$venv_dir/bin/activate"
    source "$HOME/.bashrc"
fi

"""

    def update_bashrc(self):
        print("[+] Memeriksa konfigurasi di .bashrc...")

        # Cek apakah konfigurasi sudah ada
        if os.path.exists(self.bashrc_path):
            with open(self.bashrc_path, "r") as bashrc:
                content = bashrc.read()
                if self.config.strip() in content:
                    print("[✔] Konfigurasi sudah ada, tidak perlu ditambahkan.")
                    return

        # Jika belum ada, tambahkan konfigurasi
        with open(self.bashrc_path, "a") as bashrc:
            bashrc.write("\n" + self.config)
          
        
        print("[✔] Konfigurasi .bashrc berhasil diperbarui!")



class AndroidProjectSetup:
    def __init__(self, project_dir, sdk_dir):
        self.project_dir = project_dir
        self.sdk_dir = sdk_dir

    def create_android_template(self):
        print("[+] Membuat template proyek Android...")
        os.makedirs(self.project_dir, exist_ok=True)
        os.chdir(self.project_dir)

        # Buat folder dasar
        os.makedirs("app/src/main/java/com/example/myapp", exist_ok=True)
        os.makedirs("app/src/main/res", exist_ok=True)

        # Buat file build Gradle
        self.create_gradle_files()

        print("[✔] Template proyek Android berhasil dibuat!")

    def create_gradle_files(self):
        """ Membuat file build.gradle, gradle.properties, local.properties, dan settings.gradle """

        build_gradle_app = """\
plugins {
    id 'com.android.application'
}

android {
    compileSdkVersion 33
    namespace 'com.example.myapp'
    defaultConfig {
        applicationId "com.example.myapp"
        minSdkVersion 21
        targetSdkVersion 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.4.0'
}
"""
        build_gradle_root = """\
buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.0.2'
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
"""

        settings_gradle = """\
include ':app'
"""

        gradle_properties = """\
   org.gradle.logging.level=info
   org.gradle.caching=true
   org.gradle.daemon=true
   android.useAndroidX=true
   android.enableJetifier=true
   android.aapt2FromMavenOverride=/data/data/com.termux/files/usr/bin/aapt2
   org.gradle.jvmargs=-Xmx2g
"""

        local_properties = f"""\
sdk.dir={self.sdk_dir}
"""

        # Simpan file di lokasi yang sesuai
        with open("app/build.gradle", "w") as f:
            f.write(build_gradle_app)

        with open("build.gradle", "w") as f:
            f.write(build_gradle_root)

        with open("settings.gradle", "w") as f:
            f.write(settings_gradle)

        with open("gradle.properties", "w") as f:
            f.write(gradle_properties)

        with open("local.properties", "w") as f:
            f.write(local_properties)

    
    def create_ui_templates(self):
          base_path = "app/src/main/"
          base_ic = os.path.expanduser("~/setup")
          # Path folder yang perlu dibuat
          folders = [
              base_path + "java/com/example/myapp/",
              base_path + "res/layout/",
              base_path + "res/values/",
              base_path + "res/mipmap/"
          ]
          
          # Buat folder jika belum ada
          for folder in folders:
              os.makedirs(folder, exist_ok=True)
              
          # Path ikon saat ini dan tujuan
          icon_source = os.path.join(base_ic, "ic_launcher.png")  # Sesuaikan dengan nama file ikon
          icon_destination = os.path.join(base_path, "res/mipmap/ic_launcher.png")
          
          # Pindahkan file jika ada
          if os.path.exists(icon_source):
              shutil.move(icon_source, icon_destination)
              print(f"[✔] Ikon berhasil dipindahkan ke {icon_destination}")
          else:
              print("[✘] File ikon tidak ditemukan!")

          
          # Konten file
          main_activity_java = """\
      package com.example.myapp;
      
      import android.os.Bundle;
      import androidx.appcompat.app.AppCompatActivity;
      
      public class MainActivity extends AppCompatActivity {
          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);
          }
      }
          """
          
          activity_main_xml = """\
      <?xml version="1.0" encoding="utf-8"?>
      <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:orientation="vertical"
          android:padding="16dp">
      
          <TextView
              android:id="@+id/textView"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:text="@string/hello_world"
              android:textSize="18sp"
              android:textColor="@color/black"/>
      </LinearLayout>
          """
      
          colors_xml = """\
      <?xml version="1.0" encoding="utf-8"?>
      <resources>
          <color name="black">#000000</color>
          <color name="white">#FFFFFF</color>
      </resources>
          """
      
          strings_xml = """\
      <?xml version="1.0" encoding="utf-8"?>
      <resources>
          <string name="app_name">My App</string>
          <string name="hello_world">Hello, World!</string>
      </resources>
          """
      
          values_xml = """\
      <?xml version="1.0" encoding="utf-8"?>
      <resources>
          <!-- Bisa diisi dengan dimens, styles, dll. -->
      </resources>
          """
      
          manifest_xml = """\
      <?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myapp">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.AppCompat.Light.DarkActionBar">

        <activity android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>

    </application>
</manifest>

          """
      
          # Menyimpan file
          files = {
              "java/com/example/myapp/MainActivity.java": main_activity_java,
              "res/layout/activity_main.xml": activity_main_xml,
              "res/values/colors.xml": colors_xml,
              "res/values/strings.xml": strings_xml,
              "res/values/values.xml": values_xml,
              "AndroidManifest.xml": manifest_xml,
          }
      
          for file_path, content in files.items():
              full_path = os.path.join(base_path, file_path)
              with open(full_path, "w") as f:
                  f.write(content.strip())
      
          
          print("Template UI berhasil dibuat!")

    def build_apk(self):
          project_dir = os.path.abspath("../my-android-app")  # Pastikan path benar
          
          print(f"[+] Memulai proses build APK di: {project_dir}")
          
          if not os.path.exists(project_dir):
              print("[✘] Direktori proyek tidak ditemukan!")
              return
          
          os.chdir(project_dir)  # Berpindah ke folder proyek Android
          
          if not os.path.exists("./gradlew"):
              print("[✘] File gradlew tidak ditemukan, menjalankan wrapper...")
              subprocess.run(["gradle", "wrapper"], check=True)
          
          print("[+] Memberikan izin eksekusi ke gradlew...")
          subprocess.run(["chmod", "+x", "./gradlew"], check=True)
      
          print("[+] Menjalankan Gradle build...")
          try:
              subprocess.run(["./gradlew", "assembleDebug", "--stacktrace"], check=True)
              print("[✔] Build APK selesai!")
          except subprocess.CalledProcessError as e:
              print(f"[✘] Build gagal: {e}")
              return
      
          apk_path = "app/build/outputs/apk/debug/app-debug.apk"
          if os.path.exists(apk_path):
              print(f"[✔] APK berhasil dibuat: {apk_path}")
          else:
              print("[✘] APK tidak ditemukan! Cek log error di atas.")

    
if __name__ == "__main__":
    venv_dir = os.path.expanduser("~/dev-env")
    sdk_dir = os.path.expanduser("~/dev-sdk")
    project_dir = os.path.expanduser("~/my-android-app")
 
    # Install Paket Termux
    packages = ["git", "python", "openjdk-17", "gradle", "wget", "unzip"]
    package_installer = PackageInstaller(packages)
    package_installer.install()

    # Setup Virtual Environment
    venv_manager = VirtualEnvManager(venv_dir)
    venv_manager.create_virtualenv()
    venv_manager.activate_virtualenv()
    venv_manager.install_dependencies()
    venv_manager.activate_and_upgrade()
    venv_manager.print_success("********SUCCESSFULYY********")

    # Setup Android SDK
    sdk_manager = AndroidSDKSetup(sdk_dir)
    sdk_manager.setup_sdk()
    sdk_manager.install_platform_tools()

    # Update .bashrc
    bashrc_config = BashrcConfig(venv_dir, sdk_dir)
    bashrc_config.update_bashrc()

        # Setup Android Project
    android_project = AndroidProjectSetup(project_dir, sdk_dir)
    android_project.create_android_template()
    android_project.create_ui_templates()
  
    android_project.build_apk()
    
    venv_manager.print_success("[✔] Setup selesai!!!.")
    venv_manager.deactivate_venv()