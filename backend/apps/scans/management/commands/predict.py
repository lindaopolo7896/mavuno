"""Try the AI on image files without the API:  uv run python manage.py predict leaf1.jpg leaf2.jpg"""
from django.core.management.base import BaseCommand, CommandError

from services.ai import InvalidImage, ModelNotAvailable, predict_image


class Command(BaseCommand):
    help = "Run the plant disease model on one or more image files."

    def add_arguments(self, parser):
        parser.add_argument("images", nargs="+")

    def handle(self, *args, **options):
        for path in options["images"]:
            try:
                result = predict_image(path)
            except ModelNotAvailable as error:
                raise CommandError(str(error))
            except (InvalidImage, FileNotFoundError) as error:
                self.stderr.write(f"{path}: {error}")
                continue

            verdict = "healthy" if result.is_healthy else result.disease
            line = f"{path}: {result.plant_name} - {verdict} ({result.confidence:.1%})"
            if result.is_accepted:
                self.stdout.write(self.style.SUCCESS(line))
            else:
                self.stdout.write(self.style.WARNING(f"{line}  REJECTED: {result.rejection}, would ask for a retake"))
            details = []
            if result.not_a_leaf_confidence is not None:
                details.append(f"not a leaf {result.not_a_leaf_confidence:.1%}")
            if result.similarity is not None:
                details.append(f"similarity {result.similarity:.3f}")
            if details:
                self.stdout.write("    " + ", ".join(details))
            for p in result.top:
                self.stdout.write(f"    {p['confidence']:7.2%}  {p['label']}")
