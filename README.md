I am really lazy person, so instead of having separate repository for each project I put together selection of my most used libraries into one single maven tree.

There are three major roots:

# ee.fj.io

Input - output utiles

- [Configuration file helper](io/config)
- [Password encrypted files](io/passwordfile)
- [Predict file encoding](io/smartreader)
- [Csv / Excel files wrapper](io/tablereader)

# ee.fj.javafx

Javafx helpers

- [JavaFx FXML and controller loader](javafx/loader)
- [JavaFx Single threaded scheduler](javafx/concurrent)
- [Simple wrapper to read FXML files](javafx/control)
- [Custom input validators](javafx/validator)

# ee.fj.l

- [Lightweight localization helper](l1xn/l10n)

# ee.fj.utils

- [Predict the column type in array based on the matchers](utils/columnpredictor)
- [File mime type detector if Files.probeContentType is not enough](utils/filetypes)
- [Simple algorithm for two way numbers encoding](utils/idencoder)
- [Mava mail wrapper to ease up mail sending](utils/mailer)
- [Loose version to check if one version is after another one](utils/looseversion)

# ee.fj.awt

- [Scale and resize images](awt)


# ee.fj.classloader

- [Simple whole purpose classloader with custom annotation support](classloader)

# Installation

See subprojects for more information
