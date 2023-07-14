package com.brahvim.nerd.openal;

import java.util.Objects;
import java.util.Vector;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdModuleSettings;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilderSettings;

public class NerdOpenAlModule extends NerdModule {

	public static class NerdOpenAlModuleSettings extends NerdModuleSettings<NerdOpenAlModule> {

		public int frequency;
		public int monoSources;
		public int stereoSources;
		public int refresh;
		public boolean sync;

		public NerdOpenAlModuleSettings() {
		}

		public NerdOpenAlModuleSettings(final int p_frequency, final int p_monoSources,
				final int p_stereoSources, final int p_refresh, final boolean p_sync) {
			this.frequency = p_frequency;
			this.monoSources = p_monoSources;
			this.stereoSources = p_stereoSources;
			this.refresh = p_refresh;
			this.sync = p_sync;
		}

		public AlContext.AlContextSettings asAlContextSettings() {
			final AlContext.AlContextSettings toRet = new AlContext.AlContextSettings();

			toRet.frequency = this.frequency;
			toRet.monoSources = this.monoSources;
			toRet.stereoSources = this.stereoSources;
			toRet.refresh = this.refresh;
			toRet.sync = this.sync;

			return toRet;
		}

	}

	protected final Vector<NerdAlUpdater> MANAGERS = new Vector<>(1);

	// region Construction.
	public NerdOpenAlModule(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	@Override
	protected void assignModuleSettings(final NerdModuleSettings<?> p_settings) {
		// This *secretly*, also acts as a check for `null`!:
		if (p_settings instanceof final NerdOpenAlModuleSettings moduleSettings)
			this.MANAGERS.add(this.createAlMan(moduleSettings.asAlContextSettings()));
	}
	// endregion

	public NerdAlUpdater getAlUpdater(final int p_index) {
		return this.MANAGERS.get(p_index);
	}

	// region `NerdSketch` events!
	@Override
	public void sketchConstructed(final NerdSketchBuilderSettings p_settings) {
		// When the scene is changed, delete unnecessary OpenAL data:
		super.SKETCH.getNerdModule(NerdScenesModule.class)
				.addNewSceneStartedListener((a, p, c) -> { // A mispronounced alphabets joke!
					if (p != null) // If this is not the first scene,
						for (final NerdAlUpdater m : this.MANAGERS)
							m.MAN.scenelyDisposal(); // ..We delete data.
				});
	}

	@Override
	protected void postSetup() {
		for (final NerdAlUpdater m : this.MANAGERS)
			// if (m.trackedCamIsDefaultCam)
			m.trackedCamera = super.SKETCH.getNerdGraphics().getCurrentCamera();
	}

	@Override
	protected void draw() {
		for (final NerdAlUpdater m : this.MANAGERS)
			m.framelyCallback();
	}

	@Override
	protected void dispose() {
		// When the NerdSketch is exiting, delete all OpenAL native data:
		for (final NerdAlUpdater m : this.MANAGERS)
			m.MAN.completeDisposal();
	}
	// endregion

	// region `NerdAl` creation methods.
	public NerdAlUpdater createAlMan() {
		final NerdAlUpdater toRet = new NerdAlUpdater(super.SKETCH, new NerdAl());
		this.MANAGERS.add(toRet);
		return toRet;
	}

	public NerdAlUpdater createAlMan(final String p_deviceName) {
		final NerdAlUpdater toRet = new NerdAlUpdater(super.SKETCH, new NerdAl(p_deviceName));
		this.MANAGERS.add(toRet);
		return toRet;
	}

	public NerdAlUpdater createAlMan(final AlContext.AlContextSettings p_settings) {
		final NerdAlUpdater toRet = new NerdAlUpdater(super.SKETCH, new NerdAl(p_settings));
		this.MANAGERS.add(toRet);
		return toRet;
	}

	public NerdAlUpdater createAlMan(final AlContext.AlContextSettings p_settings, final String p_deviceName) {
		final NerdAlUpdater toRet = new NerdAlUpdater(super.SKETCH, new NerdAl(p_settings, p_deviceName));
		this.MANAGERS.add(toRet);
		return toRet;
	}
	// endregion

	public void forEachManager(final Consumer<NerdAlUpdater> p_task) {
		Objects.requireNonNull(p_task);

		for (final NerdAlUpdater m : this.MANAGERS)
			p_task.accept(m);
	}

}
