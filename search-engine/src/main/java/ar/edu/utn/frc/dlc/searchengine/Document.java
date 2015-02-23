package ar.edu.utn.frc.dlc.searchengine;

import java.io.Serializable;

public class Document implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path;
	private int id;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
