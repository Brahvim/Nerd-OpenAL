package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdAlExt;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class OggBufferDataAsset extends NerdSinglePathAssetLoader<AlOggBuffer> {

	public OggBufferDataAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected AlOggBuffer fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			final AlOggBuffer oggBuffer = new AlOggBuffer((NerdAl) p_sketch.getNerdExt(NerdAlExt.class));
			oggBuffer.shouldDispose(false);
			oggBuffer.loadFrom(super.path);
			return oggBuffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException();
			// return ShortBuffer.allocate(0);
		}
	}

}
