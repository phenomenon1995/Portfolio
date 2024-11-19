def main():
    name=input("File Name: ").strip().lower()
    ext=name.rpartition(".")[2]
    print(search(ext))
    #print(ext)
def search(filename):
    match filename:
        case "jpeg"|"jpg":
            return "image/jpeg"
        case "png":
            return "image/png"
        case "gif":
            return "image/gif"
        case "pdf":
            return "application/pdf"
        case "txt":
            return "text/plain"
        case "zip":
            return "application/zip"
        case _:
            return "application/octet-stream"

main()