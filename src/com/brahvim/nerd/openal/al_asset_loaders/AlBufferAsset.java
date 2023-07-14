package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.openal.AlBuffer;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdAlUpdater;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public abstract class AlBufferAsset<AlBufferT extends AlBuffer<?>> extends NerdSinglePathAssetLoader<AlBufferT> {

	protected final NerdAlUpdater MAN;
	protected final boolean WILL_AUTO_DISPOSE;

	// region Constructors.
	/**
	 * Loads an {@link AlBuffer}, then calls {@link AlBuffer#setDisposability} on
	 * it, with {@code true} as the parameter.
	 *
	 * @param p_path is a string denoting the path to a file containing data to
	 *               be filled into the buffer.
	 */
	protected AlBufferAsset(final NerdAlUpdater p_alMan, final String p_path) {
		this(p_alMan, p_path, true);
	}

	/**
	 * Loads an {@link AlBuffer} that is disposed off after the current
	 * {@link NerdScene} is exited.
	 *
	 * @param p_path is a string denoting the path to a file containing data to
	 *               be filled into the buffer.
	 */
	protected AlBufferAsset(final NerdAlUpdater p_alMan, final String p_path, final boolean p_autoDispose) {
		super(p_path);
		this.MAN = p_alMan;
		this.WILL_AUTO_DISPOSE = p_autoDispose;
	}
	// endregion

	@Override
	protected AlBufferT fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {

			final AlBufferT alBuffer = this.createBuffer(this.MAN.getUnderlyingManager());
			alBuffer.setDisposability(this.WILL_AUTO_DISPOSE);
			alBuffer.loadFrom(super.path);
			return alBuffer;

		} catch (final Exception e) {
			throw new NerdAssetLoaderException(this, e);
		}
	}

	protected abstract AlBufferT createBuffer(final NerdAl p_alMan);

}
