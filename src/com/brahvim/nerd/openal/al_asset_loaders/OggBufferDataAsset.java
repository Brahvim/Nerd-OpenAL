package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class OggBufferDataAsset extends AssetLoader<AlOggBuffer> {

	@Override
	public AlOggBuffer fetchData(final Sketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			final AlOggBuffer oggBuffer = new AlOggBuffer((NerdAl) p_sketch.getNerdExt("OpenAL"));
			oggBuffer.shouldDispose(false);
			oggBuffer.loadFrom(p_path);
			return oggBuffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
			// return ShortBuffer.allocate(0);
		}
	}

}
