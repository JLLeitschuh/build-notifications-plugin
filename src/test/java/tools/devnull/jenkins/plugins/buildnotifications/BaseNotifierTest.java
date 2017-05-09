package tools.devnull.jenkins.plugins.buildnotifications;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import org.junit.Test;
import tools.devnull.kodo.Spec;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static tools.devnull.jenkins.plugins.buildnotifications.BuildChain.broken;
import static tools.devnull.jenkins.plugins.buildnotifications.BuildChain.fixed;
import static tools.devnull.jenkins.plugins.buildnotifications.BuildChain.stillBroken;
import static tools.devnull.jenkins.plugins.buildnotifications.BuildChain.successful;
import static tools.devnull.kodo.Expectation.because;
import static tools.devnull.kodo.Expectation.to;

public class BaseNotifierTest {

  @Test
  public void testGlobalTargetConfigurationWithoutFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, null, null, null, false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().beNull(), because("Flag is unset"))

        .when(performOn(broken()))
        .expect(target(), to().be("globalTarget"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("globalTarget"))

        .when(performOn(fixed()))
        .expect(target(), to().be("globalTarget"));
  }

  @Test
  public void testGlobalTargetConfigurationWithFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, null, null, null, true);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().be("globalTarget"), because("Flag is set"))

        .when(performOn(broken()))
        .expect(target(), to().be("globalTarget"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("globalTarget"))

        .when(performOn(fixed()))
        .expect(target(), to().be("globalTarget"));
  }

  @Test
  public void testSuccessfulTargetConfigurationWithoutGlobalTarget() {
    NotifierTest notifier = new NotifierTest(null, "successfulTarget", null, null, null, false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().be("successfulTarget"))

        .when(performOn(broken()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(stillBroken()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(fixed()))
        .expect(target(), to().beNull(), because("Global target is unset"));
  }

  @Test
  public void testSuccessfulTargetConfigurationWithGlobalTargetWithoutFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", "successfulTarget", null, null, null, false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().be("successfulTarget"))

        .when(performOn(broken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(fixed()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"));
  }

  @Test
  public void testSuccessfulTargetConfigurationWithGlobalTargetWithFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, null, null, null, true);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().be("globalTarget"), because("Global target and flag are set"))

        .when(performOn(broken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(fixed()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"));
  }

  @Test
  public void testBrokenTargetConfigurationWithoutGlobalTarget() {
    NotifierTest notifier = new NotifierTest(null, null, "brokenTarget", null, null, false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(broken()))
        .expect(target(), to().be("brokenTarget"))

        .when(performOn(stillBroken()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(fixed()))
        .expect(target(), to().beNull(), because("Global target is unset"));
  }

  @Test
  public void testBrokenTargetConfigurationWithGlobalTargetWithoutFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, "brokenTarget", null, null, false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().beNull(), because("Global target is set but the flag is not"))

        .when(performOn(broken()))
        .expect(target(), to().be("brokenTarget"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(fixed()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"));
  }

  @Test
  public void testBrokenTargetConfigurationWithGlobalTargetWithFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, "brokenTarget", null, null, true);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().be("globalTarget"), because("Global target and flag are set"))

        .when(performOn(broken()))
        .expect(target(), to().be("brokenTarget"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(fixed()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"));
  }

  @Test
  public void testStillBrokenTargetConfigurationWithoutGlobalTarget() {
    NotifierTest notifier = new NotifierTest(null, null, null, "stillBrokenTarget", null, false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(broken()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("stillBrokenTarget"))

        .when(performOn(fixed()))
        .expect(target(), to().beNull(), because("Global target is unset"));
  }

  @Test
  public void testStillBrokenTargetConfigurationWithGlobalTargetWithoutFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, null, "stillBrokenTarget", null, false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().beNull(), because("Global target is set but the flag is not"))

        .when(performOn(broken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("stillBrokenTarget"))

        .when(performOn(fixed()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"));
  }

  @Test
  public void testStillBrokenTargetConfigurationWithGlobalTargetWithFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, null, "stillBrokenTarget", null, true);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().be("globalTarget"), because("Global target and flag are set"))

        .when(performOn(broken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("stillBrokenTarget"))

        .when(performOn(fixed()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"));
  }

  @Test
  public void testFixedTargetConfigurationWithoutGlobalTarget() {
    NotifierTest notifier = new NotifierTest(null, null, null, null, "fixedTarget", false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(broken()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(stillBroken()))
        .expect(target(), to().beNull(), because("Global target is unset"))

        .when(performOn(fixed()))
        .expect(target(), to().be("fixedTarget"));
  }

  @Test
  public void testFixedTargetConfigurationWithGlobalTargetWithoutFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, null, null, "fixedTarget", false);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().beNull(), because("Global target is set but the flag is not"))

        .when(performOn(broken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(fixed()))
        .expect(target(), to().be("fixedTarget"));
  }

  @Test
  public void testFixedTargetConfigurationWithGlobalTargetWithFlag() {
    NotifierTest notifier = new NotifierTest("globalTarget", null, null, null, "fixedTarget", true);
    Spec.given(notifier)
        .when(performOn(successful()))
        .expect(target(), to().be("globalTarget"), because("Global target and flag are set"))

        .when(performOn(broken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(stillBroken()))
        .expect(target(), to().be("globalTarget"), because("Global target is set"))

        .when(performOn(fixed()))
        .expect(target(), to().be("fixedTarget"));
  }

  private Function<NotifierTest, String> target() {
    return NotifierTest::target;
  }

  private Consumer<NotifierTest> performOn(AbstractBuild build) {
    return notifier -> {
      try {
        notifier.perform(build, null, null);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    };
  }

  private class NotifierTest extends BaseNotifier {

    private String target;

    public NotifierTest(String globalTarget,
                        String successfulTarget,
                        String brokenTarget,
                        String stillBrokenTarget,
                        String fixedTarget,
                        boolean sendIfSuccess) {
      super(globalTarget, successfulTarget, brokenTarget, stillBrokenTarget, fixedTarget, sendIfSuccess);
    }

    @Override
    protected String getBaseUrl() {
      return null;
    }

    @Override
    protected Message createMessage(String target, AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
      this.target = target;
      return mock(Message.class);
    }

    public String target() {
      try {
        return this.target;
      } finally {
        this.target = null;
      }
    }

  }

}
