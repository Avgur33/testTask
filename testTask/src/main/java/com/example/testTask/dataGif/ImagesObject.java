package com.example.testTask.dataGif;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagesObject {

    private Object fixed_height;
    private Object fixed_height_still;
    private Object fixed_height_downsampled;
    private Object fixed_width;
    private Object fixed_width_still;
    private Object fixed_width_downsampled;
    private Object fixed_height_small;
    private Object fixed_height_small_still;
    private Object fixed_width_small;
    private Object fixed_width_small_still;
    private Object downsized;
    private Object downsized_still;
    private Object downsized_large;
    private Object downsized_medium;
    private Object downsized_small;
    private Object original;
    private Object original_still;
    private Object looping;
    private Object preview;
    private Object preview_gif;

    public Object getFixed_height() {
        return fixed_height;
    }

    public void setFixed_height(Object fixed_height) {
        this.fixed_height = fixed_height;
    }

    public Object getFixed_height_still() {
        return fixed_height_still;
    }

    public void setFixed_height_still(Object fixed_height_still) {
        this.fixed_height_still = fixed_height_still;
    }

    public Object getFixed_height_downsampled() {
        return fixed_height_downsampled;
    }

    public void setFixed_height_downsampled(Object fixed_height_downsampled) {
        this.fixed_height_downsampled = fixed_height_downsampled;
    }

    public Object getFixed_width() {
        return fixed_width;
    }

    public void setFixed_width(Object fixed_width) {
        this.fixed_width = fixed_width;
    }

    public Object getFixed_width_still() {
        return fixed_width_still;
    }

    public void setFixed_width_still(Object fixed_width_still) {
        this.fixed_width_still = fixed_width_still;
    }

    public Object getFixed_width_downsampled() {
        return fixed_width_downsampled;
    }

    public void setFixed_width_downsampled(Object fixed_width_downsampled) {
        this.fixed_width_downsampled = fixed_width_downsampled;
    }

    public Object getFixed_height_small() {
        return fixed_height_small;
    }

    public void setFixed_height_small(Object fixed_height_small) {
        this.fixed_height_small = fixed_height_small;
    }

    public Object getFixed_height_small_still() {
        return fixed_height_small_still;
    }

    public void setFixed_height_small_still(Object fixed_height_small_still) {
        this.fixed_height_small_still = fixed_height_small_still;
    }

    public Object getFixed_width_small() {
        return fixed_width_small;
    }

    public void setFixed_width_small(Object fixed_width_small) {
        this.fixed_width_small = fixed_width_small;
    }

    public Object getFixed_width_small_still() {
        return fixed_width_small_still;
    }

    public void setFixed_width_small_still(Object fixed_width_small_still) {
        this.fixed_width_small_still = fixed_width_small_still;
    }

    public Object getDownsized() {
        return downsized;
    }

    public void setDownsized(Object downsized) {
        this.downsized = downsized;
    }

    public Object getDownsized_still() {
        return downsized_still;
    }

    public void setDownsized_still(Object downsized_still) {
        this.downsized_still = downsized_still;
    }

    public Object getDownsized_large() {
        return downsized_large;
    }

    public void setDownsized_large(Object downsized_large) {
        this.downsized_large = downsized_large;
    }

    public Object getDownsized_medium() {
        return downsized_medium;
    }

    public void setDownsized_medium(Object downsized_medium) {
        this.downsized_medium = downsized_medium;
    }

    public Object getDownsized_small() {
        return downsized_small;
    }

    public void setDownsized_small(Object downsized_small) {
        this.downsized_small = downsized_small;
    }

    public Object getOriginal() {
        return original;
    }

    public void setOriginal(Object original) {
        this.original = original;
    }

    public Object getOriginal_still() {
        return original_still;
    }

    public void setOriginal_still(Object original_still) {
        this.original_still = original_still;
    }

    public Object getLooping() {
        return looping;
    }

    public void setLooping(Object looping) {
        this.looping = looping;
    }

    public Object getPreview() {
        return preview;
    }

    public void setPreview(Object preview) {
        this.preview = preview;
    }

    public Object getPreview_gif() {
        return preview_gif;
    }

    public void setPreview_gif(Object preview_gif) {
        this.preview_gif = preview_gif;
    }
}
