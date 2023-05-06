package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class OggBufferDataAsset extends NerdAssetLoader<AlOggBuffer> {

	@Override
	public AlOggBuffer fetchData(final NerdSketch p_NerdSketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			final AlOggBuffer oggBuffer = new AlOggBuffer((NerdAl) p_NerdSketch.getNerdExt("OpenAL"));
			oggBuffer.shouldDispose(false);
			oggBuffer.loadFrom(p_path);
			return oggBuffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException();
			// return ShortBuffer.allocate(0);
		}
	}

}
