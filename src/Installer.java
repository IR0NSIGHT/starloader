import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;
import java.util.zip.ZipEntry;

public class Installer {
    public static void install(File smJarFile) throws IOException {
        FileAdder.mkDefaults();

        String classFileFolder = filePath("out/production/StarLoader");
        File newJarF = File.createTempFile("ModloaderTmp", ".jar");
        JarOutputStream newJar = new JarOutputStream(new FileOutputStream(newJarF));
        JarInputStream inputS = new JarInputStream(new FileInputStream(smJarFile));


        ArrayList<File> resources = new ArrayList<>();
        for (String s : FileAdder.filesToAdd){
            resources.add(new File(classFileFolder + File.separator + s));
            System.out.println("Added " + s);
        }
        //resources.add(new File(classFileFolder + "/SMModLoader.class"));
        writeJar(inputS, newJar, resources);


        newJar.close();
        Files.move(newJarF.toPath(), new File("StarMade.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Done");
        JOptionPane.showMessageDialog(null, "StarLoader installed!");
    }

    static void writeJar(JarInputStream in, JarOutputStream out, List<File> insert) throws IOException {
        if (in.getManifest() != null) {
            in.getManifest().getMainAttributes().putValue(Attributes.Name.MAIN_CLASS.toString(), "SMModLoader");
            ZipEntry me = new ZipEntry(JarFile.MANIFEST_NAME);
            out.putNextEntry(me);
            in.getManifest().write(out);
            out.closeEntry();
        }
        //big buffer for big writes
        byte[] buffer = new byte[1 << 14];
        for (File extraFile : insert) {
            writeSpecialEntry(extraFile, out);
        }

        for (JarEntry je; (je = in.getNextJarEntry()) != null; ) {
            writeEntry(je, buffer, in, out);
        }

        in.close();
    }

    static void writeSpecialEntry(File toWrite, JarOutputStream out) {
        System.out.println("Writing: " + toWrite.getName());
        try {
            String path = toWrite.getPath();
            System.out.println(path);

            //Delete the out/production/StarLoader/ part so.
            out.putNextEntry(new JarEntry(path.replace("out" + File.separator + "production" + File.separator + "StarLoader" + File.separator, "")));

            //Write file
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            FileInputStream input = new FileInputStream(toWrite);
            while ((bytesRead = input.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeEntry(JarEntry toWrite, byte[] buffer, InputStream in, JarOutputStream out) throws IOException {
        System.out.println("Writing: " + toWrite.getName());
        out.putNextEntry(toWrite);
        for (int nr; 0 < (nr = in.read(buffer)); ) {
            out.write(buffer, 0, nr);
        }
    }

    static String filePath(String s) {
        return s.replace(".", File.separator);
    }
}
