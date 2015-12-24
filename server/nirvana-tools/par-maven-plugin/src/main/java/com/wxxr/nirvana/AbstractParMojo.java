package com.wxxr.nirvana;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;

/**
 * Base class for creating a jar from project classes.
 *
 * @author <a href="evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 */
public abstract class AbstractParMojo extends AbstractMojo {

	private static final String[] DEFAULT_EXCLUDES = new String[] { "**/package.html" };

	private static final String[] DEFAULT_INCLUDES = new String[] { "**/**" };

	/**
	 * Single directory for extra files to include in the WAR. This is where you
	 * place your JSP js css files.
	 */
	@Parameter(defaultValue = "${basedir}/src/main/webapp", required = true)
	private File sourceDirectory;

	/**
	 * Single directory for extra files to include in the WAR. This is where you
	 * place your JSP js css files.
	 */
	@Parameter(defaultValue = "${basedir}/webplugin", required = true)
	private File webplugin;

	/**
     */
	@Parameter(defaultValue = "${project.build.directory}", required = true)
	private File targetDir;

	/**
     */
	@Parameter(defaultValue = "${project.build.finalName}.jar", required = true)
	String jarFileName;
	/**
     */
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}", required = true)
	private File appDirectory;

	/**
     */
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}/lib", required = true)
	private File libDirectory;

	/**
	 * The directory where the webapp is built.
	 */
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}/html", required = true)
	private File htmlDirectory;

	/**
     */
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}/message", required = true)
	private File messageDirectory;

	/**
	 * List of files to include. Specified as fileset patterns which are
	 * relative to the input directory whose contents is being packaged into the
	 * JAR.
	 */
	@Parameter
	private String[] includes;

	/**
	 * List of files to exclude. Specified as fileset patterns which are
	 * relative to the input directory whose contents is being packaged into the
	 * JAR.
	 */
	@Parameter
	private String[] excludes;

	/**
	 * Directory containing the generated JAR.
	 */
	@Parameter(defaultValue = "${project.build.directory}", required = true)
	private File outputDirectory;

	/**
	 * Name of the generated JAR.
	 */
	@Parameter(alias = "parName", property = "par.finalName", defaultValue = "${project.build.finalName}")
	private String finalName;

	// /**
	// * The Jar archiver.
	// */
	// @Component(role = Archiver.class, hint = "par")
	// private JarArchiver parArchiver;

	/**
	 * The Maven project.
	 */
	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	/**
     *
     */
	@Parameter(defaultValue = "${session}", readonly = true, required = true)
	private MavenSession session;

	/**
	 * The archive configuration to use. See <a
	 * href="http://maven.apache.org/shared/maven-archiver/index.html">Maven
	 * Archiver Reference</a>.
	 */
	@Parameter
	private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

	/**
	 * Path to the default MANIFEST file to use. It will be used if
	 * <code>useDefaultManifestFile</code> is set to <code>true</code>.
	 *
	 * @since 2.2
	 */
	@Parameter(defaultValue = "${project.build.outputDirectory}/META-INF/MANIFEST.MF", required = true, readonly = true)
	private File defaultManifestFile;

	/**
	 * Set this to <code>true</code> to enable the use of the
	 * <code>defaultManifestFile</code>.
	 *
	 * @since 2.2
	 */
	@Parameter(property = "jar.useDefaultManifestFile", defaultValue = "false")
	private boolean useDefaultManifestFile;

	/**
     *
     */
	@Component
	private MavenProjectHelper projectHelper;

	/**
	 * Require the jar plugin to build a new JAR even if none of the contents
	 * appear to have changed. By default, this plugin looks to see if the
	 * output jar exists and inputs have not changed. If these conditions are
	 * true, the plugin skips creation of the jar. This does not work when other
	 * plugins, like the maven-shade-plugin, are configured to post-process the
	 * jar. This plugin can not detect the post-processing, and so leaves the
	 * post-processed jar in place. This can lead to failures when those plugins
	 * do not expect to find their own output as an input. Set this parameter to
	 * <tt>true</tt> to avoid these problems by forcing this plugin to recreate
	 * the jar every time.
	 */
	@Parameter(property = "par.forceCreation", defaultValue = "false")
	private boolean forceCreation;

	/**
	 * Skip creating empty archives
	 */
	@Parameter(property = "par.skipIfEmpty", defaultValue = "false")
	private boolean skipIfEmpty;

	/**
	 * Return the specific output directory to serve as the root for the
	 * archive.
	 */
	protected abstract File getClassesDirectory();

	protected final MavenProject getProject() {
		return project;
	}

	/**
	 * Overload this to produce a jar with another classifier, for example a
	 * test-jar.
	 */
	protected abstract String getClassifier();

	/**
	 * Overload this to produce a test-jar, for example.
	 */
	protected abstract String getType();

	protected static File getTargetFile(File basedir, String finalName,
			String classifier, String type) {
		if (classifier == null) {
			classifier = "";
		} else if (classifier.trim().length() > 0
				&& !classifier.startsWith("-")) {
			classifier = "-" + classifier;
		}

		return new File(basedir, finalName + classifier + "." + type);
	}

	/**
	 * Default Manifest location. Can point to a non existing file. Cannot
	 * return null.
	 */
	protected File getDefaultManifestFile() {
		return defaultManifestFile;
	}

	/**
	 * Generates the JAR.
	 *
	 * @todo Add license files in META-INF directory.
	 */
	public File createArchive() throws MojoExecutionException {

		File parFile = getTargetFile(outputDirectory, finalName,
				getClassifier(), "par");

		MavenArchiver archiver = new MavenArchiver();

		JarArchiver parArchiver = new JarArchiver();
		archiver.setArchiver(parArchiver);

		archiver.setOutputFile(parFile);

		archive.setForced(forceCreation);

		try {
			archiver.getArchiver().addDirectory(getAppDirectory(),
					getIncludes(), getExcludes());

			File existingManifest = getDefaultManifestFile();

			if (useDefaultManifestFile && existingManifest.exists()
					&& archive.getManifestFile() == null) {
				getLog().info(
						"Adding existing MANIFEST to archive. Found under: "
								+ existingManifest.getPath());
				archive.setManifestFile(existingManifest);
			}

			archiver.createArchive(session, project, archive);

			return parFile;
		} catch (Exception e) {
			throw new MojoExecutionException("Error assembling PAR", e);
		}
	}

	/**
	 * Generates the JAR.
	 *
	 * @todo Add license files in META-INF directory.
	 */
	public void execute() throws MojoExecutionException {
		if (skipIfEmpty
				&& (!getClassesDirectory().exists() || getClassesDirectory()
						.list().length < 1)) {
			getLog().info("Skipping packaging of the " + getType());
		} else {

			// 打包 jar
			// createJavaArchive();
			// 创建par包的目录
			try {
				buildExplodedApp();
			} catch (MojoFailureException e) {
				throw new MojoExecutionException("create explode dir error ", e);
			}
			File parFile = createArchive();
			String classifier = getClassifier();
			if (classifier != null) {
				projectHelper.attachArtifact(getProject(), getType(),
						classifier, parFile);
			} else {
				getProject().getArtifact().setFile(parFile);
			}
		}
	}

	/**
	 * 应该是jar的优先级高，所以先执行了所以这里不用执行
	 * 
	 * @throws MojoExecutionException
	 */
	private void createJavaArchive() throws MojoExecutionException {
		File jarFile = getTargetFile(outputDirectory, finalName,
				getClassifier(), "jar");

		JarArchiver jararchiver = new JarArchiver();
		MavenArchiver archiver = new MavenArchiver();
		archiver.setArchiver(jararchiver);
		archiver.setOutputFile(jarFile);
		archive.setForced(forceCreation);
		File contentDirectory = getClassesDirectory();
		if (contentDirectory.exists()) {
			archiver.getArchiver().addDirectory(contentDirectory,
					getIncludes(), getExcludes());
			File existingManifest = getDefaultManifestFile();

			if (useDefaultManifestFile && existingManifest.exists()
					&& archive.getManifestFile() == null) {
				getLog().info(
						"Adding existing MANIFEST to archive. Found under: "
								+ existingManifest.getPath());
				archive.setManifestFile(existingManifest);
			}
			try {
				archiver.createArchive(session, project, archive);
			} catch (Exception e) {
				throw new MojoExecutionException("Error assembling PAR", e);
			}
		}
	}

	public void buildExplodedApp() throws MojoExecutionException,
			MojoFailureException {
		getAppDirectory().mkdirs();
		getLibDirectory().mkdirs();
		getMessageDirectory().mkdirs();
		getHtmlDirectory().mkdirs();
		getHtmlDirectory().mkdirs();
		// copy jar --> lib/
		File jar = new File(getTargetDir(), getJarFileName());
		if (jar.exists()) {
			getLog().info(
					"copy: " + jar.getPath() + " to "
							+ getLibDirectory().getPath());
			try {
				copyFileToDirectory(jar, getLibDirectory());
			} catch (IOException e) {
				throw new MojoExecutionException("copy jar error ", e);
			}
		}

		// copy html --> html
		try {
			copyDirToDir(getSourceDirectory(), getHtmlDirectory(), null);
			getLog().info(
					"copy: " + getSourceDirectory().getPath() + " to "
							+ getHtmlDirectory().getPath());
		} catch (IOException e) {
			throw new MojoExecutionException("copy html error ", e);
		}

		// copy webplugin.xml --> webplugin
		File webplugin = new File(getWebplugin(), "webplugin.xml");
		try {
			copyFileToDirectory(webplugin, getAppDirectory());
		} catch (IOException e) {
			throw new MojoExecutionException("copy jar error ", e);
		}

	}

	public void copyDirToDir(File srcDir, File destDir, FileFilter filter)
			throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (srcDir.exists() && srcDir.isDirectory() == false) {
			throw new IllegalArgumentException("Source '" + destDir
					+ "' is not a directory");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new IllegalArgumentException("Destination '" + destDir
					+ "' is not a directory");
		}
		List<String> exclusionList = null;
		if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
			File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir
					.listFiles(filter);
			if (srcFiles != null && srcFiles.length > 0) {
				exclusionList = new ArrayList<String>(srcFiles.length);
				for (File srcFile : srcFiles) {
					File copiedFile = new File(destDir, srcFile.getName());
					exclusionList.add(copiedFile.getCanonicalPath());
				}
			}
		}
		doCopyDirectory(srcDir, destDir, filter, exclusionList);
	}

	private void doCopyDirectory(File srcDir, File destDir, FileFilter filter,
			List<String> exclusionList) throws IOException {
		// recurse
		File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir
				.listFiles(filter);
		if (srcFiles == null) { // null if abstract pathname does not denote a
								// directory, or if an I/O error occurs
			throw new IOException("Failed to list contents of " + srcDir);
		}
		if (destDir.exists()) {
			if (destDir.isDirectory() == false) {
				throw new IOException("Destination '" + destDir
						+ "' exists but is not a directory");
			}
		} else {
			if (!destDir.mkdirs() && !destDir.isDirectory()) {
				throw new IOException("Destination '" + destDir
						+ "' directory cannot be created");
			}
		}
		if (destDir.canWrite() == false) {
			throw new IOException("Destination '" + destDir
					+ "' cannot be written to");
		}
		for (File srcFile : srcFiles) {
			File dstFile = new File(destDir, srcFile.getName());
			if (exclusionList == null
					|| !exclusionList.contains(srcFile.getCanonicalPath())) {
				if (srcFile.isDirectory()) {
					doCopyDirectory(srcFile, dstFile, filter, exclusionList);
				} else {
					doCopyFile(srcFile, dstFile);
				}
			}
		}

	}

	private final int buffersize = 1024 * 1024 * 30;

	private void copyFileToDirectory(File srcFile, File destDir)
			throws IOException {
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new IllegalArgumentException("Destination '" + destDir
					+ "' is not a directory");
		}
		File destFile = new File(destDir, srcFile.getName());
		copyFile(srcFile, destFile);
	}

	private void copyFile(File srcFile, File destFile) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (srcFile.exists() == false) {
			throw new FileNotFoundException("Source '" + srcFile
					+ "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile
					+ "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '"
					+ destFile + "' are the same");
		}
		File parentFile = destFile.getParentFile();
		if (parentFile != null) {
			if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
				throw new IOException("Destination '" + parentFile
						+ "' directory cannot be created");
			}
		}
		if (destFile.exists() && destFile.canWrite() == false) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is read-only");
		}
		doCopyFile(srcFile, destFile);

	}

	private void doCopyFile(File src, File destFile) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is a directory");
		}
		FileChannel sourceCh = null;
		FileChannel destCh = null;

		try {
			FileInputStream fis = new FileInputStream(src);
			FileOutputStream fos = new FileOutputStream(destFile);
			sourceCh = fis.getChannel();
			destCh = fos.getChannel();

			long size = sourceCh.size();
			long pos = 0;
			long count = 0;
			while (pos < size) {
				count = size - pos > buffersize ? buffersize : size - pos;
				pos += destCh.transferFrom(sourceCh, pos, count);
			}
		} catch (Exception e) {
			throw new IOException("copy file error ", e);
		} finally {
			try {
				sourceCh.close();
				destCh.close();
			} catch (IOException e) {
				getLog().warn(e);
			}
		}

	}

	private String[] getIncludes() {
		if (includes != null && includes.length > 0) {
			return includes;
		}
		return DEFAULT_INCLUDES;
	}

	private String[] getExcludes() {
		if (excludes != null && excludes.length > 0) {
			return excludes;
		}
		return DEFAULT_EXCLUDES;
	}

	public File getAppDirectory() {
		return appDirectory;
	}

	public void setAppDirectory(File appDirectory) {
		this.appDirectory = appDirectory;
	}

	public File getLibDirectory() {
		return libDirectory;
	}

	public void setLibDirectory(File libDirectory) {
		this.libDirectory = libDirectory;
	}

	public File getHtmlDirectory() {
		return htmlDirectory;
	}

	public void setHtmlDirectory(File htmlDirectory) {
		this.htmlDirectory = htmlDirectory;
	}

	public File getMessageDirectory() {
		return messageDirectory;
	}

	public void setMessageDirectory(File messageDirectory) {
		this.messageDirectory = messageDirectory;
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public File getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(File targetDir) {
		this.targetDir = targetDir;
	}

	public String getJarFileName() {
		return jarFileName;
	}

	public void setJarFileName(String jarFileName) {
		this.jarFileName = jarFileName;
	}

	public File getWebplugin() {
		return webplugin;
	}

	public void setWebplugin(File webplugin) {
		this.webplugin = webplugin;
	}
}
