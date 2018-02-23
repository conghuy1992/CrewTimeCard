package timecard.dazone.com.dazonetimecard.dtos;

public class ProductDto extends MenuDto {
    public GCMDto gcmDto;
    public String link;
    public ProductDto(String title, int icon) {
        super(title, icon);
    }

    public ProductDto() {
        super();
    }
    public ProductDto(int title, int icon) {
        super(title, icon);
    }
    public ProductDto(int titleID, int icon, boolean Hide) {
        super(titleID, icon, Hide);
    }

    public ProductDto(int titleID, int icon, GCMDto gcmDto) {
        super(titleID, icon);
        this.gcmDto = gcmDto;
    }
    public void setGCM(GCMDto gcmDto) {
        this.gcmDto = gcmDto;
    }
    public void setLink(String link) {
        this.link = link;
    }
}
