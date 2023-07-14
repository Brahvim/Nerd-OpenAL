package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.openal.AlBuffer;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdOpenAlModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public abstract class AlBufferAsset<AlBufferT extends AlBuffer<?>> extends NerdSinglePathAssetLoader<AlBufferT> {

	protected boolean shouldDispose;

	protected AlBufferAsset(final String p_path, final boolean p_autoDispose) {
		super(p_path);
		this.shouldDispose = p_autoDispose;
	}

	@Override
	protected AlBufferT fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			final NerdAl alMan = p_sketch.getNerdModule(NerdOpenAlModule.class).getOpenAlManager();

			final AlBufferT alBuffer = this.createBuffer(alMan);
			alBuffer.setDisposability(this.shouldDispose);
			alBuffer.loadFrom(super.path);

			return alBuffer;
		} catch (final Exception e) {
			throw new NerdAssetLoaderException(this, e);
		}
	}

	protected abstract AlBufferT createBuffer(final NerdAl p_alMan);

}
