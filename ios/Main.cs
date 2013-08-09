using System;
using MonoTouch.Foundation;
using MonoTouch.UIKit;

using playn.ios;
using playn.core;
using com.threerings.perf.core;

namespace com.threerings.perf
{
  [Register ("AppDelegate")]
  public partial class AppDelegate : IOSApplicationDelegate {
    public override bool FinishedLaunching (UIApplication app, NSDictionary options) {
      app.SetStatusBarHidden(true, true);
      IOSPlatform.Config config = new IOSPlatform.Config();
      config.orients = IOSPlatform.SupportedOrients.PORTRAITS;
      IOSPlatform.register(app, config);
      PlayN.run(new PerfTest());
      return true;
    }
  }

  public class Application {
    static void Main (string[] args) {
      UIApplication.Main (args, null, "AppDelegate");
    }
  }
}
