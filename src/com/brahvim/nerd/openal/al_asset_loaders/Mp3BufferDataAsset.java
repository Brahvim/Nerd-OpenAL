package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlMp3Buffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;

@Deprecated
public class Mp3BufferDataAsset extends AssetLoader<AlMp3Buffer> {

	@Override
	public AlMp3Buffer fetchData(final Sketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			final AlMp3Buffer mp3Buffer = new AlMp3Buffer((NerdAl) p_sketch.getNerdExt("OpenAL"));
			mp3Buffer.shouldDispose(false);
			mp3Buffer.loadFrom(p_path);
			return mp3Buffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
			// return ShortBuffer.allocate(0);
		}
	}

}
