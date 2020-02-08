import javax.swing.*;
import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class Installer {
    public static void install(File smJarFile) throws IOException {
        FileAdder.mkDefaults();

        String classFileFolder = filePath("classes");
        File newJarF = File.createTempFile("ModloaderTmp", ".jar");
        FileOutputStream newJarOutputStream = new FileOutputStream(newJarF);
        JarOutputStream newJar = new JarOutputStream(newJarOutputStream);
        FileInputStream oldJarInputStream = new FileInputStream(smJarFile);
        JarInputStream inputS = new JarInputStream(oldJarInputStream);


        ArrayList<File> resources = new ArrayList<>();
        for (String s : FileAdder.filesToAdd){
            resources.add(new File(classFileFolder + File.separator + s));
            System.out.println("Added " + s);
        }
        //resources.add(new File(classFileFolder + "/SMModLoader.class"));
        writeJar(inputS, newJar, resources);


        newJar.close();
        inputS.close();
        oldJarInputStream.close();
        newJarOutputStream.close();

        try {
            //Files.move(newJarF.toPath(), new File("StarMade.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
            //FIXME: throws an error... somehow the jar is being used
            Files.move(newJarF.toPath(), smJarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (FileSystemException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Filesystem error, make sure starmade is NOT open");
        }
        System.out.println("Done");

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
        out.close();
        in.close();
    }

    static void writeSpecialEntry(File toWrite, JarOutputStream out) {
        System.out.println("Writing: " + toWrite.getName());
        try {
            String path = toWrite.getPath();
            System.out.println(path);

            //Delete the out/production/StarLoader/ part so.
            out.putNextEntry(new JarEntry(path.replace("classes" + File.separator, "")));

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
        try {
            out.putNextEntry(toWrite);
            for (int nr; 0 < (nr = in.read(buffer)); ) {
                out.write(buffer, 0, nr);
            }
        }catch (ZipException e){
            System.out.println(e.getMessage());
            System.out.println("No problem really");
        }
    }

    static String filePath(String s) {
        return s.replace(".", File.separator);
    }
}
