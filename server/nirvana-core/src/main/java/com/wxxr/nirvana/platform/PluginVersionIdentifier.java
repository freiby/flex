package com.wxxr.nirvana.platform;

import org.apache.commons.lang3.StringUtils;

public class PluginVersionIdentifier implements
		Comparable<PluginVersionIdentifier> {

	private int major;
	private int minor;
	private int revision;
	private String buildId;

	public PluginVersionIdentifier() {
	}

	public PluginVersionIdentifier(int major, int minor, int revision,
			String buildId) {
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.buildId = buildId;

	}

	public PluginVersionIdentifier(String version) {
		if (StringUtils.isBlank(version)) {
			throw new IllegalArgumentException();
		}
		String[] vs = version.split("\\.");
		if ((vs.length < 2) || (!StringUtils.isNumeric(vs[0]))
				|| (!StringUtils.isNumeric(vs[1])) || (vs.length > 3)) {
			throw new IllegalArgumentException("Invalid version :" + version);
		}
		if (vs.length > 2) {
			try {
				int idx = vs[2].indexOf('_');
				if (idx != -1) {
					revision = Integer.parseInt(vs[2].substring(0, idx));
					buildId = vs[2].substring(idx + 1);
				} else {
					revision = Integer.parseInt(vs[2]);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid version :"
						+ version);
			}
		}
		major = Integer.parseInt(vs[0]);
		minor = Integer.parseInt(vs[1]);
	}

	/**
	 * @return the major
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * @param major
	 *            the major to set
	 */
	public void setMajor(int major) {
		this.major = major;
	}

	/**
	 * @return the minor
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * @param minor
	 *            the minor to set
	 */
	public void setMinor(int minor) {
		this.minor = minor;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer().append(major).append('.')
				.append(minor).append('.').append(revision);
		if (StringUtils.isNotBlank(buildId)) {
			buf.append('_').append(buildId);
		}
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(PluginVersionIdentifier o) {
		if (o == null) {
			return 1;
		}
		return toString().compareTo(o.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buildId == null) ? 0 : buildId.hashCode());
		result = prime * result + major;
		result = prime * result + minor;
		result = prime * result + revision;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PluginVersionIdentifier other = (PluginVersionIdentifier) obj;
		if (buildId == null) {
			if (other.buildId != null)
				return false;
		} else if (!buildId.equals(other.buildId))
			return false;
		if (major != other.major)
			return false;
		if (minor != other.minor)
			return false;
		if (revision != other.revision)
			return false;
		return true;
	}

}
