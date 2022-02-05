package info.itsthesky.itemcreator.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public final class FileUtils {

	public static void writeFile(File file, String content) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("ALL")
	public static @Nullable String retrieveFile(File file) {
		try {
			return Files.toString(file, Charsets.UTF_8);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
