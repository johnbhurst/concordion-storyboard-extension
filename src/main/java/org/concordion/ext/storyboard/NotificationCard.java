package org.concordion.ext.storyboard;

import java.io.OutputStream;

import org.concordion.api.Element;
import org.concordion.api.Resource;

public class NotificationCard extends Card {
	private String dataFileName = "";
	private CardImage cardImage;
	private String data = "";
	private String fileExtension = "txt";

	public void setCardImage(final CardImage cardImage) {
		this.cardImage = cardImage;
	}

	public void setData(final String data) {
		if (data == null) {
			this.data = "";
		} else {
			this.data = data;
		}
	}

	@Override
	protected void captureData() {
		if (data != null && !data.isEmpty()) {
			dataFileName = getFileName(getResource().getName(), getCardNumber(), fileExtension);
			Resource xmlResource = getResource().getRelativeResource(dataFileName);

			try {
				// As don't have access to the concordion spec, store the results for later
				OutputStream outputStream = getTarget().getOutputStream(xmlResource);
				outputStream.write(data.getBytes());
				this.dataFileName = xmlResource.getName();
			} catch (Exception e) {
				// Unable to write file
				this.dataFileName = "";
			}

			data = "";
		}
	}

	@Override
	protected void addHTMLToContainer(final Element storyboard, final Element container) {
		String imageName = getResource().getRelativePath(cardImage.getResource());

		Element img = new Element("img");
		img.setId(this.getDescription());
		img.addStyleClass("sizeheight");
		img.addAttribute("src", imageName);

		if (dataFileName.isEmpty()) {
			container.appendChild(img);
		} else {
			Element anchorImg = new Element("a");
			anchorImg.addAttribute("href", dataFileName);
			container.appendChild(anchorImg);

			anchorImg.appendChild(img);
		}
	}

	public void setFileExtension(final String fileExtension) {
		if (fileExtension == null || fileExtension.isEmpty()) {
			return;
		}

		if (fileExtension.startsWith(".")) {
			this.fileExtension = fileExtension.substring(1);
		} else {
			this.fileExtension = fileExtension;
		}
	}
}
